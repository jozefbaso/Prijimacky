package com.jozefbaso.prijimackykody;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

import javax.xml.datatype.Duration;

public class MainActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;

    private String barcodeData;

    private TextView scannedCode;
    private TextView scannedStudent;
    private Button exportBtn;
    private Button saveBtn;
    private Switch subjectSwitch;
    private EditText editText;

    Students listOfStudents;
    Student.Subject currentSubject;
    Student currentStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        surfaceView = findViewById(R.id.surface_view);
        scannedCode = findViewById(R.id.textViewCode);
        scannedStudent = findViewById(R.id.textViewStudent);
        loadStudents();
        saveBtn = findViewById(R.id.buttonSave);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                savePoints();
            }
        });
        exportBtn = findViewById(R.id.buttonExport);
        exportBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                exportStudents();
            }
        });
        subjectSwitch = findViewById(R.id.switch1);
        subjectSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchSubject();
                System.out.println("current subject is: " + currentSubject);
            }
        });
        switchSubject();
        editText = findViewById(R.id.editText);
    }

    private boolean isOnList(String barcode){
        return listOfStudents.Students.get(barcode) != null;
    }

    private void loadStudents(){
        listOfStudents = new Students();
        listOfStudents.addStudent(new Student("Janko", "Mrkvicka", "A35F28Z"));
        listOfStudents.addStudent(new Student("Anicka", "Dusicka", "R45S77I"));
    }
    private void exportStudents(){
        Set<String> newRows = listOfStudents.Students.keySet();
        System.out.println("===================================");
        for (String barcode : newRows) {
            System.out.println(listOfStudents.Students.get(barcode));
        }
    }

    private void setCurrentStudent(String barcode){
        if(isOnList(barcode)){
            currentStudent = new Student(listOfStudents.Students.get(barcode).getFirstName(),listOfStudents.Students.get(barcode).getLastName(),barcode);
        }
        else {
            currentStudent = new Student("","--- neplatný kód ---",barcode);
        }
        System.out.println("current student is: " + currentStudent.getFirstName() + " " + currentStudent.getLastName());
        scannedStudent.setText(currentStudent.getFirstName() + " " + currentStudent.getLastName());
    }

    private void switchSubject(){
        if(subjectSwitch.isChecked()) currentSubject = Student.Subject.SJL;
        else if(!subjectSwitch.isChecked()) currentSubject = Student.Subject.MAT;
    }

    private void savePoints(){
        if(isOnList(currentStudent.getCode())){
        listOfStudents.addPoints(currentStudent.getCode(),Double.parseDouble(editText.getText().toString()), currentSubject);
            Toast toast = Toast.makeText(getApplicationContext(),"zapisane \n" + currentStudent.getFirstName() + " " + currentStudent.getLastName() + "\n" + editText.getText().toString()  + " bodov\n" + currentSubject,Toast.LENGTH_SHORT);
            toast.show();
        System.out.println("zapisane " + editText.getText().toString()  + " bodov zo " + currentSubject + " pre studenta: " + currentStudent.getFirstName() + " " + currentStudent.getLastName());
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(), "nie je možné uložiť,\n kód nie je priradený k žiadnemu študentovi",Toast.LENGTH_SHORT);
            toast.show();
            System.out.println("cannot save points, student not found");
        }
    }

    private void initialiseDetectorsAndSources() {

        //Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1000, 500)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    scannedCode.post(new Runnable() {

                        @Override
                        public void run() {
                            System.out.println("text scanned");
                                barcodeData = barcodes.valueAt(0).displayValue;
                                scannedCode.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                                setCurrentStudent(barcodeData);
                        }
                    });

                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Objects.requireNonNull(getSupportActionBar()).hide();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Objects.requireNonNull(getSupportActionBar()).hide();
        initialiseDetectorsAndSources();
    }

}