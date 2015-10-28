package cz.muni.fi.pv256.uco374366;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

public class FilmActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.film_detail_fragment);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        TextView textViewTitle = (TextView) findViewById(R.id.title);
        textViewTitle.setText(intent.getStringExtra("TITLE"));

        TextView textViewReleaseDate = (TextView) findViewById(R.id.releaseDate);
        textViewReleaseDate.setText(Long.toString(intent.getLongExtra("RELEASE_DATE", -1)));

        ImageView imageViewCover = (ImageView) findViewById(R.id.cover);
        imageViewCover.setImageResource(intent.getIntExtra("COVER", -1));
    }

}
