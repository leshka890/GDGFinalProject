package ru.gdgkazan.footbalproject.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import ru.gdgkazan.footbalproject.api.ApiFactory;
import ru.gdgkazan.footbalproject.model.content.Fixture;
import ru.gdgkazan.footbalproject.model.content.Player;
import ru.gdgkazan.footbalproject.model.content.Standings;
import ru.gdgkazan.footbalproject.model.content.Team;
import ru.gdgkazan.footbalproject.model.response.FixturesListResponse;
import ru.gdgkazan.footbalproject.model.response.PlayersResponse;
import ru.gdgkazan.footbalproject.model.response.StandingsListResponse;
import ru.gdgkazan.footbalproject.model.response.TeamResponse;
import ru.gdgkazan.footbalproject.model.response.TeamsResponse;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Alexey Antonchik on 17.09.16.
 */
public class DefaulFootballRepository implements FootballRepository {

    @Override
    public Observable<List<Fixture>> fixtures() {
        return ApiFactory.getFootballService()
                .fixtures()
                .map(FixturesListResponse::getFixtures)
                .flatMap(fixtures -> {
                    Realm.getDefaultInstance().executeTransaction(realm -> {
                        RealmResults<Fixture> fixtureRealmResults = realm.where(Fixture.class).findAll();
                        fixtureRealmResults.deleteAllFromRealm();
                        realm.insert(fixtures);
                    });
                    return Observable.just(fixtures);
                })
                .onErrorResumeNext(throwable -> {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<Fixture> fixtureRealmResults = realm.where(Fixture.class).findAll();

                    if(fixtureRealmResults.size() > 0) {
                        return Observable.just(realm.copyFromRealm(fixtureRealmResults));
                    }else {
                        return Observable.error(new NoSuchElementException());
                    }
                });
    }

    @Override
    public Observable<List<Standings>> standingsList() {
        return ApiFactory.getFootballService().standingsList()
                .map(StandingsListResponse::getStandingsList)
                .flatMap(standingsList -> {
                        Realm.getDefaultInstance().executeTransaction(realm -> {
                            realm.delete(Standings.class);
                            realm.insert(standingsList);
                        });
                        return Observable.just(standingsList);
                    }
                )
                .onErrorResumeNext(throwable -> {
                        Realm realm = Realm.getDefaultInstance();
                        RealmResults<Standings> realmResultsStandingsList = realm.where(Standings.class).findAll();
                        if(realmResultsStandingsList.size() > 0) {
                            return Observable.just(realm.copyFromRealm(realmResultsStandingsList));
                        }else {
                            return Observable.error(new NoSuchElementException());
                        }
                    }
                );
    }

    @Override
    public Observable<Team> team(String teamName) {
        return Observable.just(0)
                .flatMap(integer -> {
                    Team team = Realm.getDefaultInstance().where(Team.class).equalTo(Team.FIELD_NAME,teamName).findFirst();
                    if(team != null) {
                        return getTeamById(team.getId());
                    } else {
                        return getTeamFromAllTeams(teamName);
                    }
                })
                .flatMap(this::getTeamWithPlayers);
    }

    private Observable<Team> getTeamFromAllTeams(String teamName) {
        return ApiFactory.getFootballService()
                .teams()
                .map(TeamsResponse::getTeams)
                .flatMap(Observable::from)
                .flatMap(this::teamMapper)
                .toList()
                .flatMap(teams -> {
                    Realm.getDefaultInstance().executeTransaction(realm -> {
                        realm.delete(Team.class);
                        realm.insert(teams);
                    });
                    return Observable.from(teams);
                })
                .filter(team -> teamName.equals(team.getName()))
                .switchIfEmpty(Observable.error(new NoSuchElementException()));
    }

    private Observable<Team> getTeamById(int teamId) {
        return ApiFactory.getFootballService()
                .team(teamId)
                .flatMap(this::teamMapper)
                .flatMap(team -> {
                    Realm.getDefaultInstance().executeTransaction(realm -> realm.insertOrUpdate(team));
                    return Observable.just(team);
                })
                .onErrorResumeNext(throwable -> {
                    Realm realm = Realm.getDefaultInstance();
                    Team team = realm.where(Team.class).equalTo(Team.FIELD_ID,teamId).findFirst();
                    return Observable.just(realm.copyFromRealm(team));
                });
    }

    @NonNull
    private Observable<Team> getTeamWithPlayers(Team team) {
        return ApiFactory.getFootballService()
                .players(team.getId())
                .map(PlayersResponse::getPlayers)
                .flatMap(players -> {
                    Realm realmInstance = Realm.getDefaultInstance();
                    realmInstance.executeTransaction(realm -> {
                        Team teamRealm = realm.where(Team.class).equalTo(Team.FIELD_ID, team.getId()).findFirst();
                        teamRealm.getPlayers().deleteAllFromRealm();
                        teamRealm.getPlayers().addAll(players);
                    });

                    Team teamRealm = realmInstance.where(Team.class).equalTo(Team.FIELD_ID, team.getId()).findFirst();
                    return Observable.just(realmInstance.copyFromRealm(teamRealm));
                })
                .onErrorReturn(throwable -> team);
    }

    @NonNull
    private Observable<Team> teamMapper(@NonNull TeamResponse response) {
        String link = response.getLinks().getSelf().getHref();
        Matcher m = Pattern.compile("\\d{2,}")
                .matcher(link);
        if(m.find()) {
            int id = Integer.parseInt(m.group());
            return Observable.just(new Team(
                    id,
                    response.getName(),
                    response.getCode(),
                    response.getShortName(),
                    response.getSquadMarketValue(),
                    response.getCrestUrl()));
        } else {
            return Observable.error(new NoSuchElementException());
        }
    }



}