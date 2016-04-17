package mavidser.textemoticons;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by sid on 22/2/16.
 */
public class EmoticonIME extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView kv;
    private Keyboard keyboard;
    InputMethodManager imeManager;
    IBinder token;
    private Keyboard tablesKeyboard;
    private Keyboard facesKeyboard;

    private boolean caps = false;
    private int mKeyboardState;
    @Override
    public View onCreateInputView() {
        imeManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.emoticon_main);
        tablesKeyboard= new Keyboard(this, R.xml.emoticon_main, R.integer.table);
        facesKeyboard = new Keyboard(this, R.xml.emoticon_main, R.integer.faces);
        mKeyboardState = R.integer.faces;
        token = this.getWindow().getWindow().getAttributes().token;
        kv.setKeyboard(facesKeyboard);
        kv.setOnKeyboardActionListener(this);
        kv.setPreviewEnabled(false);
        return kv;
    }

    private void playClick(int keyCode){
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        switch(keyCode){
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default: am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        Log.d("Key Pressed", primaryCode+"");
        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);
        switch(primaryCode){
            case Keyboard.KEYCODE_DELETE :
                ic.deleteSurroundingText(1, 0);
                break;
            case -10:
                imeManager.switchToNextInputMethod(token, false);
                break;
            case -100:
                kv.setKeyboard(facesKeyboard);
                break;
            case -101:
                kv.setKeyboard(tablesKeyboard);
                break;
            default:
                char code = (char)primaryCode;
                ic.commitText(String.valueOf(code),1);
        }
    }

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {
        Log.d("Text Received", text.toString());
        InputConnection ic = getCurrentInputConnection();
        ic.commitText(text,1);
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeLeft() {
        Log.d("Swipe","Left");
    }

    @Override
    public void swipeRight() {
        Log.d("Swipe","Right");
    }

    @Override
    public void swipeUp() {
    }
}
