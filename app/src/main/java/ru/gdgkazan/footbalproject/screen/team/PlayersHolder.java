package ru.gdgkazan.footbalproject.screen.team;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.gdgkazan.footbalproject.R;
import ru.gdgkazan.footbalproject.model.content.Player;
import ru.gdgkazan.footbalproject.utils.CountryCodes;
import ru.gdgkazan.footbalproject.utils.Images;

/**
 * Created by Sergei Riabov
 */
class PlayersHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.name)
    TextView mNameText;

    @BindView(R.id.position)
    TextView mPositionText;

    @BindView(R.id.number)
    TextView mNumberText;

    @BindView(R.id.birth)
    TextView mBirthText;

    @BindView(R.id.country)
    ImageView mCountryView;

    PlayersHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void bind(@NonNull Player player){
        CountryCodes countryCodes = new CountryCodes();
        String code = countryCodes.getCode(player.getNationality());
        Images.loadCountryFlag(mCountryView, code);

        mNameText.setText(player.getName());
        mPositionText.setText(player.getPosition());
        mBirthText.setText(mBirthText.getResources().getString(R.string.player_age, getPlayerAge(player.getDateOfBirth())));
        if(player.getJerseyNumber() != null) {
            mNumberText.setText(String.valueOf(player.getJerseyNumber()));
        } else {
            mNumberText.setText(R.string.not_available);
        }
    }

    private int getPlayerAge(Date birthDate) {
        Calendar todayCal = GregorianCalendar.getInstance();
        Calendar bitrhCal = GregorianCalendar.getInstance();
        bitrhCal.setTime(birthDate);

        todayCal.add(Calendar.DAY_OF_YEAR, -bitrhCal.get(Calendar.DAY_OF_YEAR));

        return todayCal.get(Calendar.YEAR) - bitrhCal.get(Calendar.YEAR);
    }
}
