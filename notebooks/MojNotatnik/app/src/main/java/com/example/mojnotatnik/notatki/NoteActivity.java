package com.example.mojnotatnik.notatki;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mojnotatnik.R;
import com.example.mojnotatnik.settings.MyVibration;

public class NoteActivity extends AppCompatActivity {

    private boolean mIsViewingOrUpdating;
    private long mNoteCreationTime;
    private String mFileName;
    private Note mLoadedNote = null;

    private EditText mEtTitle;
    private EditText mEtContent;

    private MyVibration myVibration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mEtTitle = findViewById(R.id.note_et_title);
        mEtContent = findViewById(R.id.note_et_content);

        myVibration = new MyVibration();

        mFileName = getIntent().getStringExtra(Utilities.EXTRAS_NOTE_FILENAME);
        if(mFileName != null && !mFileName.isEmpty() && mFileName.endsWith(Utilities.FILE_EXTENSION)) {
            mLoadedNote = Utilities.getNoteByFileName(getApplicationContext(), mFileName);
            if (mLoadedNote != null) {
                mEtTitle.setText(mLoadedNote.getTitle());
                mEtContent.setText(mLoadedNote.getContent());
                mNoteCreationTime = mLoadedNote.getDateTime();
                mIsViewingOrUpdating = true;
            }
        } else {
            mNoteCreationTime = System.currentTimeMillis();
            mIsViewingOrUpdating = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(mIsViewingOrUpdating) {
            getMenuInflater().inflate(R.menu.menu_note_view, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_note_new, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save_note:
            case R.id.action_update:
                makeSingeVibration();
                validateAndSaveNote();
                break;
            case R.id.action_delete:
                makeDoubleVibration();
                actionDelete();
                break;
            case R.id.action_cancel:
                actionCancel();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        actionCancel();
    }

    private void makeSingeVibration(){
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (myVibration.isActive()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(myVibration.getTime(), VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                vibrator.vibrate(myVibration.getTime());
            }
        }
    }

    private void makeDoubleVibration(){
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        try {
            if (myVibration.isActive()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(myVibration.getTime(), VibrationEffect.DEFAULT_AMPLITUDE));
                    Thread.sleep(200);
                    vibrator.vibrate(VibrationEffect.createOneShot(myVibration.getTime(), VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    vibrator.vibrate(myVibration.getTime());
                    Thread.sleep(200);
                    vibrator.vibrate(myVibration.getTime());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void actionDelete() {
        makeSingeVibration();
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(this)
                .setTitle("Usuniecie notatki = (")
                .setMessage("Usunąć napewno?")

                .setPositiveButton("TAK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mLoadedNote != null && Utilities.deleteFile(getApplicationContext(), mFileName)) {
                            makeSingeVibration();
                            Toast.makeText(NoteActivity.this, " Usunięto notatkę: " +  mLoadedNote.getTitle()
                                    , Toast.LENGTH_SHORT).show();
                        } else {
                            makeDoubleVibration();
                            Toast.makeText(NoteActivity.this, "Nie można usunąć notatkę: " + mLoadedNote.getTitle()
                                    , Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                })
                .setNegativeButton("NIE", null);

        dialogDelete.show();
    }


    private void actionCancel() {
        if(!checkNoteAlerted()) {
            makeSingeVibration();
            finish();
        } else {
            makeSingeVibration();
            AlertDialog.Builder dialogCancel = new AlertDialog.Builder(this)
                    .setTitle("Skasowanie zmian!")
                    .setMessage("Napewno nie chcesz zapisać zmian?")
                    .setPositiveButton("TAK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            makeSingeVibration();
                            finish();
                        }
                    })
                    .setNegativeButton("NIE", null);
            dialogCancel.show();
        }
    }


    private boolean checkNoteAlerted() {
        if(mIsViewingOrUpdating) {
            return mLoadedNote != null && (!mEtTitle.getText().toString().equalsIgnoreCase(mLoadedNote.getTitle())
                    || !mEtContent.getText().toString().equalsIgnoreCase(mLoadedNote.getContent()));
        } else {
            return !mEtTitle.getText().toString().isEmpty() || !mEtContent.getText().toString().isEmpty();
        }
    }

    private void validateAndSaveNote() {

        String title = mEtTitle.getText().toString();
        String content = mEtContent.getText().toString();

        if(title.isEmpty()) {
            makeSingeVibration();
            Toast.makeText(NoteActivity.this, "Wprowadż nadgłówek!"
                    , Toast.LENGTH_SHORT).show();
            return;
        }

        if(content.isEmpty()) {
            makeDoubleVibration();
            Toast.makeText(NoteActivity.this, "Wprowadż treść notatki!"
                    , Toast.LENGTH_SHORT).show();
            return;
        }

        if(mLoadedNote != null) {
            mNoteCreationTime = mLoadedNote.getDateTime();
        } else {
            mNoteCreationTime = System.currentTimeMillis();
        }

        if(Utilities.saveNote(this, new Note(mNoteCreationTime, title, content))) {
            makeSingeVibration();
            Toast.makeText(this, "Notatka: " + mEtTitle.getText().toString() + " została zapisana!", Toast.LENGTH_SHORT).show();
        } else {
            makeDoubleVibration();
            Toast.makeText(this, "Nie można zapisać notatkę =( \nSprawdz czy masz pamięć!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
