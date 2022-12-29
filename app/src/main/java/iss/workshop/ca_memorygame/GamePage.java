package iss.workshop.ca_memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GamePage extends AppCompatActivity implements View.OnClickListener {

    ArrayList<Integer> gridIDs = new ArrayList<>();
    ArrayList<Integer> assignedPositions = new ArrayList<>();
    HashMap<Integer, Integer> gameAnswer = new HashMap<>();
    HashMap<Integer, Integer> imageToDisplay = new HashMap<>();
    ArrayList<String> testImageURLs = new ArrayList<String>(){{
        add("https://cdn.stocksnap.io/img-thumbs/280h/tree-desert_JMTQNOJWU8.jpg");
        add("https://cdn.stocksnap.io/img-thumbs/280h/lily-pad_A6ZEIQ32GJ.jpg");
        add("https://cdn.stocksnap.io/img-thumbs/280h/woman-portrait_XOXDWWH2PE.jpg");
        add("https://cdn.stocksnap.io/img-thumbs/280h/flower-garden_3JEBUQCKBH.jpg");
        add("https://cdn.stocksnap.io/img-thumbs/280h/father-child_PYULJR0I6W.jpg");
        add("https://cdn.stocksnap.io/img-thumbs/280h/cardinal-bird_6LHN3IBD9W.jpg");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);

        ImageView img1 = findViewById(R.id.image1);
        img1.setOnClickListener(this);
        gridIDs.add(img1.getId());
        Glide.with(this)
                .load("https://cdn.stocksnap.io/img-thumbs/280h/tree-desert_JMTQNOJWU8.jpg")
                .override(50,50).into(img1);

        ImageView img2 = findViewById(R.id.image2);
        img2.setOnClickListener(this);
        gridIDs.add(img2.getId());

        ImageView img3 = findViewById(R.id.image3);
        img3.setOnClickListener(this);
        gridIDs.add(img3.getId());

        ImageView img4 = findViewById(R.id.image4);
        img4.setOnClickListener(this);
        gridIDs.add(img4.getId());

        ImageView img5 = findViewById(R.id.image5);
        img5.setOnClickListener(this);
        gridIDs.add(img5.getId());

        ImageView img6 = findViewById(R.id.image6);
        img6.setOnClickListener(this);
        gridIDs.add(img6.getId());

        ImageView img7 = findViewById(R.id.image7);
        img7.setOnClickListener(this);
        gridIDs.add(img7.getId());

        ImageView img8 = findViewById(R.id.image8);
        img8.setOnClickListener(this);
        gridIDs.add(img8.getId());

        ImageView img9 = findViewById(R.id.image9);
        img9.setOnClickListener(this);
        gridIDs.add(img9.getId());

        ImageView img10 = findViewById(R.id.image10);
        img10.setOnClickListener(this);
        gridIDs.add(img10.getId());

        ImageView img11 = findViewById(R.id.image11);
        img11.setOnClickListener(this);
        gridIDs.add(img11.getId());

        ImageView img12 = findViewById(R.id.image12);
        img12.setOnClickListener(this);
        gridIDs.add(img12.getId());

        //Assigning 2 random numbers (btw 0 - 11) to each image
        while (assignedPositions.size() < 12){
            Random randomNum = new Random();
            int position = randomNum.nextInt(12);
            if (!assignedPositions.contains(position)){
                assignedPositions.add(position);
            }
        };

        //Create a dictionary containing the ids of an image and its twin
        int counter = 0;
        for(int i = 0; i < 6; i ++){
            System.out.println("Start");
            System.out.println("Counter: " + counter);
            System.out.println("Counter: " + (counter +1));
            //Position 1
            int imageposition1 = assignedPositions.get(counter);
            System.out.println("Image Position 1: " + imageposition1);
            //Position 2
            int imageposition2 = assignedPositions.get(counter + 1);
            System.out.println("Image Position 2: " + imageposition2);
            int imageID1 = gridIDs.get(imageposition1);
            int imageID2 = gridIDs.get(imageposition2);
            gameAnswer.put(imageID1, imageID2);
            gameAnswer.put(imageID2, imageID1);
            System.out.println("GameAnswer " + imageID1 + " :" + gameAnswer.get(imageID1));
            System.out.println("GameAnswer " + imageID2 + " :" + gameAnswer.get(imageID2));
            imageToDisplay.put(imageID1, (i + 1));
            imageToDisplay.put(imageID2, (i + 1));
            System.out.println("Image to Display " + imageposition1 + " :" + (i + 1));
            System.out.println("Image to Display " + imageposition2 + " :" + (i + 1));
            System.out.println("End");
            counter += 2;
        }
    }

    @Override
    public void onClick(View v){
        int id = v.getId();
        System.out.println(id);

    }
}