package ru.gdgkazan.footbalproject.test;

import java.util.ArrayList;
import java.util.List;

import ru.gdgkazan.footbalproject.model.content.Fixture;
import ru.gdgkazan.footbalproject.model.content.Standings;
import ru.gdgkazan.footbalproject.model.content.Team;
import ru.gdgkazan.footbalproject.repository.FootballRepository;
import rx.Observable;

/**
 * @author Sergei Riabov
 */
public class TestFootballRepository implements FootballRepository {

    private boolean returnError = true;
    private ArrayList<Fixture> mFixtures;
    private List<Standings> mStandingsList;

    public TestFootballRepository(boolean returnError) {
        this.returnError = returnError;
    }

    public TestFootballRepository(boolean returnError, ArrayList<Fixture> fixtures) {
        this.returnError = returnError;
        mFixtures = fixtures;
    }

    public TestFootballRepository(boolean returnError, List<Standings> standingsList){
        this.returnError = returnError;
        mStandingsList = standingsList;
    }

    @Override
    public Observable<List<Fixture>> fixtures() {
        if(returnError) return Observable.error(new Exception());
        return Observable.just(mFixtures);
    }

    @Override
    public Observable<List<Standings>> standingsList() {
        if(returnError){
            return Observable.error(new Exception());
        }
        return Observable.just(mStandingsList);
    }

    @Override
    public Observable<Team> team(String teamName) {
        if(returnError) {
            return Observable.error(new Exception());
        } else {
            return Observable.just(new Team());
        }
    }
}

