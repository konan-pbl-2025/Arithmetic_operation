package com.example.arithmetic_operation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class Normal extends AppCompatActivity {

    private TextView textView1;
    private TextView textView2,total_count,timer;
    private Button kakutei;
    private Button[] numButtons, opButtons;

    private String left = "", op = "", right = "", lastType = "";
    private Button leftBtn, rightBtn;
    private boolean firstDone = false, okMode = false, lastNumPressed = false;
    private int count = 0;

    // ---- タイマー関連 ----ここから追加
    private Handler timerHandler = new Handler();
    private long startTime = 0L;
    private boolean timerRunning = false;
    private Button reset;//これ追加
    private String[] resetVals = new String[5];//これ追加
    private boolean isNewGame = true;

    @Override
    protected void onResume() {
        super.onResume();

        if (isNewGame) {
            resetTimer(); // 表示と時間をリセット
            startTimer();
            isNewGame = false;
        }
    }


    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (!timerRunning) return;

            long millis = System.currentTimeMillis() - startTime;
            int minutes = (int) (millis / 60000);
            int seconds = (int) ((millis / 1000) % 60);
            int centiseconds = (int) ((millis % 1000) / 10);

            String time = String.format("%02d:%02d:%02d", minutes, seconds, centiseconds);
            timer.setText(time);

            timerHandler.postDelayed(this, 10);
        }
    };

    private void startTimer() {
        startTime = System.currentTimeMillis();
        timerRunning = true;
        timerHandler.post(timerRunnable);
    }

    private void stopTimer() {
        timerRunning = false;
    }

    private void resetTimer() {
        timerRunning = false;
        timer.setText("00:00:00");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arithmetic);

        textView1 = findViewById(R.id.textView1);

        textView2 = findViewById(R.id.textView2);

        kakutei = findViewById(R.id.kakutei);
        total_count = findViewById(R.id.problem);//これも　カウント用
        timer = findViewById(R.id.time); // ← タイマー表示TextView
        reset = findViewById(R.id.reset);
        numButtons = new Button[]{
                findViewById(R.id.num1), findViewById(R.id.num2),
                findViewById(R.id.num3), findViewById(R.id.num4),
                findViewById(R.id.num5)
        };
        opButtons = new Button[]{
                findViewById(R.id.plus1), findViewById(R.id.plus2),
                findViewById(R.id.plus3)
        };
        total_count.setText(count + "/3問正解");
        int[] question = Creating_Question.createNormal();

        for (int i = 0; i < numButtons.length; i++) {
            numButtons[i].setText(String.valueOf(question[i]));
        }

        textView2.setText(String.valueOf(question[5]));
        for (int i = 0; i < numButtons.length; i++) {
            resetVals[i] = numButtons[i].getText().toString(); // ← 初期値保存
        }
        reset.setOnClickListener(v->resetAll());
        //ここまで
        String[] ops = {"+", "-", "×", "÷"};

        for (int i = 0; i < opButtons.length; i++)
            opButtons[i].setText(ops[i]);

        for (Button b : numButtons) b.setOnClickListener(v -> onNum(b));
        for (Button b : opButtons) b.setOnClickListener(v -> onOp(b));
        kakutei.setOnClickListener(v -> onKakutei());

        updateStates();
        startTimer();
    }

    private void onNum(Button b) {
        String val = b.getText().toString();
        if (val.isEmpty()) return;

        if (okMode) {
            lastNumPressed = true;
            left=val;
            textView1.setText(val);
            updateStates();
            return;
        }

        if (!firstDone) {
            left = val;
            leftBtn = b;
            textView1.setText(left);
            firstDone = true;
            lastType = "num";
        } else if (!op.isEmpty()) {
            if (b == leftBtn) return;
            right = val;
            rightBtn = b;
            textView1.setText(left + op + right);
            lastType = "num";
        } else {
            left = val;
            leftBtn = b;
            textView1.setText(left);
        }
        updateStates();
    }

    private void onOp(Button b) {
        if (okMode || !firstDone || !right.isEmpty()) return;
        op = b.getText().toString();
        textView1.setText(left + op);
        lastType = "op";
        updateStates();
    }

    private void onKakutei() {
        if (okMode) {
            if (!lastNumPressed) return;
            //与えられた値と作った値が同じか
            try {
                BigDecimal l = new BigDecimal(left);
                BigDecimal target = new BigDecimal(textView2.getText().toString()); // 正しい合計値

                if (l.compareTo(target) == 0) {
                    count++;
                }

                total_count.setText(count + "/3問正解");

                if (count == 3) {
                    count = 0;
                    stopTimer();   // タイマー停止
                    Intent intent = new Intent(Normal.this, result.class);//resultActivityはリザルト用に変更
                    String timeValue = timer.getText().toString();  // 例："00:23:45"
                    intent.putExtra("TIME_VALUE", timeValue);
                    String mode="2";
                    intent.putExtra("MODE",mode);
                    isNewGame=true;
                    startActivity(intent);//これらはリザルト画面に行くときにする*/
                    onRestart();
                    return;
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            //もしそうなら回数1増やす
            resetNums();
            kakutei.setText("=");
            okMode = lastNumPressed = false;
            textView1.setText("");
            updateStates();
            return;
        }

        if (left.isEmpty() || op.isEmpty() || right.isEmpty()) return;

        String exp = left + op + right;
        String resultText;
        /*ここから*/
        try {
            BigDecimal l = new BigDecimal(left);
            BigDecimal r = new BigDecimal(right);
            BigDecimal result;

            switch (op) {
                case "+":
                    result = l.add(r);
                    break;
                case "-":
                    result = l.subtract(r);
                    break;
                case "×":
                    result = l.multiply(r);
                    break;
                case "÷":
                    if (r.compareTo(BigDecimal.ZERO) == 0) {
                        textView1.setText(exp + " = エラー");
                        op = "";          // 演算子リセット
                        right = "";       // 右辺クリア
                        rightBtn = null;  // ボタン選択解除
                        lastType = "";    // 状態リセット
                        updateStates();
                        return;
                    }
                    result = l.divide(r, 6, RoundingMode.HALF_UP); // 小数6桁まで正確に割り算
                    break;
                default:
                    return;
            }
            // 小数点以下を適切に丸め
            result = result.stripTrailingZeros();

            // 整数なら小数点なしで表示
            resultText = result.scale() <= 0 ? result.toPlainString()
                    : result.setScale(Math.min(result.scale(), 3), RoundingMode.HALF_UP).toPlainString();

            textView1.setText(exp + " = " + resultText);

            leftBtn.setText(resultText);
            rightBtn.setText("");
            rightBtn.setEnabled(false);
            rightBtn.setAlpha(0.4f);

        } catch (Exception e) {
            textView1.setText("エラー");
            return;
        }
//ここまで
        left = right = op = lastType = "";
        leftBtn = rightBtn = null;
        firstDone = false;

        if (remainingButtons() == 1) {
            kakutei.setText("OK");
            okMode = true;
            lastNumPressed = false;
        }
        updateStates();
    }

    private void updateStates() {
        if (okMode) {
            for (Button b : numButtons) {
                boolean empty = b.getText().toString().isEmpty();
                b.setEnabled(!empty);
                b.setAlpha(empty ? 0.4f : 1f);
            }
            for (Button b : opButtons) disable(b);
            kakutei.setEnabled(lastNumPressed);
            kakutei.setAlpha(lastNumPressed ? 1f : 0.4f);
            return;
        }

        for (Button b : numButtons) {
            boolean disable = (leftBtn != null && b == leftBtn && !op.isEmpty())
                    || b.getText().toString().isEmpty();
            b.setEnabled(!disable);
            b.setAlpha(!disable ? 1f : 0.4f);
        }
        for (Button b : opButtons)
            enable(b, firstDone && right.isEmpty());

        boolean canConfirm = !left.isEmpty() && !op.isEmpty() && !right.isEmpty();
        kakutei.setEnabled(canConfirm);
        kakutei.setAlpha(canConfirm ? 1f : 0.4f);
    }

    private void resetNums() {
        // 新しい問題を作る
        int[] question = Creating_Question.createNormal();

        // numButtons に question[0]〜[4] をセット
        for (int i = 0; i < numButtons.length; i++) {
            numButtons[i].setText(String.valueOf(question[i]));
            numButtons[i].setEnabled(true);
            numButtons[i].setAlpha(1f);
        }
        for (int i = 0; i < numButtons.length; i++) {
            resetVals[i] = numButtons[i].getText().toString(); // ← 初期値保存
        }
        // textView2 に question[5] をセット
        textView2.setText(String.valueOf(question[5]));
    }

    private int remainingButtons() {
        int c = 0;
        for (Button b : numButtons)
            if (!b.getText().toString().isEmpty()) c++;
        return c;
    }

    private void disable(Button b) { b.setEnabled(false); b.setAlpha(0.4f); }
    private void enable(Button b, boolean e) { b.setEnabled(e); b.setAlpha(e ? 1f : 0.4f); }
    private void resetAll() {
        left = "";
        op = "";
        right = "";
        textView1.setText("");
        firstDone=false;
        okMode=false;
        lastNumPressed=false;
        leftBtn=null;
        rightBtn=null;
        for (int i = 0; i < numButtons.length; i++) {
            numButtons[i].setEnabled(true);
            numButtons[i].setAlpha(1f);
            numButtons[i].setText(String.valueOf(resetVals[i])); // ←初期値に戻す！
        }

        rightBtn = null;
    }
}
