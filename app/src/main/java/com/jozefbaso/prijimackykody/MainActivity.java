package com.jozefbaso.prijimackykody;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.xml.datatype.Duration;

public class MainActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;

    private TextView scannedCode;
    private TextView scannedStudent;
    private EditText editText;
    private Button importBtn;
    private Button exportBtn;
    private Button saveBtn;
    private Switch subjectSwitch;


    private Map<String, Student> listOfStudents;
    Student.Subject currentSubject;
    Student currentStudent;
    String previousBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUiElements();
        loadStudents();
        switchSubject();
    }

    private void initUiElements() {
        setContentView(R.layout.activity_main);
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        surfaceView = findViewById(R.id.surface_view);
        scannedCode = findViewById(R.id.textViewCode);
        scannedStudent = findViewById(R.id.textViewStudent);
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
        importBtn = findViewById(R.id.buttonImport);
        importBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                importStudents();
            }
        });
        subjectSwitch = findViewById(R.id.switch1);
        subjectSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchSubject();
                System.out.println("current subject is: " + currentSubject);
            }
        });
        editText = findViewById(R.id.editText);
    }

    private void importStudents() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Vyberte zoznam študentov"), 33);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == 33) {
//                String inputFile = Objects.requireNonNull(data.getData()).getPath();
//                System.out.println(inputFile);
////                try {
////                    ExcelReadWrite.readExcel(listOfStudents,inputFile);
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
//            }
//        }
//    }

    private void loadStudents() {
        listOfStudents = new LinkedHashMap<>();
        listOfStudents.put("A35F28Z", new Student("Janko", "Mrkvicka", "A35F28Z", "-1", "-1"));
        listOfStudents.put("R45S77I", new Student("Anicka", "Dusicka", "R45S77I", "-1", "-1"));
    }

    private void exportStudents() {
        Set<String> newRows = listOfStudents.keySet();
        System.out.println("===================================");
        for (String barcode : newRows) {
            System.out.println(listOfStudents.get(barcode));
        }
    }

    private void setCurrentStudent(String barcode) {
        currentStudent = listOfStudents.get(barcode);
        if (currentStudent != null) {
            scannedStudent.setText(currentStudent.getFirstName() + " " + currentStudent.getLastName());
            System.out.println("current student is: " + currentStudent.getFirstName() + " " + currentStudent.getLastName());
        } else {
            scannedStudent.setText("---neplatný kód---");
        }
    }

    private void switchSubject() {
        if (subjectSwitch.isChecked()) currentSubject = Student.Subject.SJL;
        else if (!subjectSwitch.isChecked()) currentSubject = Student.Subject.MAT;
    }

    private void savePoints() {
        if (currentStudent != null) {
            currentStudent.setPoints(editText.getText().toString(), currentSubject);
            Toast.makeText(getApplicationContext(), "zapisane \n" + currentStudent.getFirstName() + " " + currentStudent.getLastName() + "\n" + editText.getText().toString() + " bodov\n" + currentSubject, Toast.LENGTH_SHORT).show();
            System.out.println("zapisane " + editText.getText().toString() + " bodov zo " + currentSubject + " pre studenta: " + currentStudent.getFirstName() + " " + currentStudent.getLastName());
        } else {
            Toast.makeText(getApplicationContext(), "nie je možné uložiť,\n kód nie je priradený k žiadnemu študentovi", Toast.LENGTH_SHORT).show();
        }
    }

    private void onDataScanned(String barcode) {
        if (barcode.equals(previousBarcode)) return;
        previousBarcode = barcode;
        scannedCode.setText(barcode);
        editText.getText().clear();
        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
        setCurrentStudent(barcode);
    }

    private void initialiseDetectorsAndSources() {
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
                            String barcodeData = barcodes.valueAt(0).displayValue;
                            onDataScanned(barcodeData);
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