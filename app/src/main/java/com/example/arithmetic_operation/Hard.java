package com.example.arithmetic_operation;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class Hard extends AppCompatActivity {

    private TextView textView;
    private Button kakutei;
    private Button[] numButtons, opButtons;

    private String left = "", op = "", right = "", lastType = "";
    private Button leftBtn, rightBtn;
    private boolean firstDone = false, okMode = false, lastNumPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView1);
        kakutei = findViewById(R.id.kakutei);

        numButtons = new Button[]{
                findViewById(R.id.num1), findViewById(R.id.num2),
                findViewById(R.id.num3), findViewById(R.id.num4),
                findViewById(R.id.num5)
        };
        opButtons = new Button[]{
                findViewById(R.id.plus1), findViewById(R.id.plus2),
                findViewById(R.id.plus3), findViewById(R.id.plus4)
        };

        Random rand = new Random();
        //ここから
        int[] initVals=Creating_Question.createHard();
        int j=0;
        for (Button b : numButtons) {
            b.setText(String.valueOf(initVals[j]));
            j++;
        }
        //ここまで
        String[] ops = {"+", "-", "×", "÷"};
        for (int i = 0; i < numButtons.length; i++)
            numButtons[i].setText(String.valueOf(initVals[i]));
        for (int i = 0; i < opButtons.length; i++)
            opButtons[i].setText(ops[i]);

        for (Button b : numButtons) b.setOnClickListener(v -> onNum(b));
        for (Button b : opButtons) b.setOnClickListener(v -> onOp(b));
        kakutei.setOnClickListener(v -> onKakutei());

        updateStates();
    }

    private void onNum(Button b) {
        String val = b.getText().toString();
        if (val.isEmpty()) return;

        if (okMode) {
            lastNumPressed = true;
            textView.setText(val);
            updateStates();
            return;
        }

        if (!firstDone) {
            left = val;
            leftBtn = b;
            textView.setText(left);
            firstDone = true;
            lastType = "num";
        } else if (!op.isEmpty()) {
            if (b == leftBtn) return;
            right = val;
            rightBtn = b;
            textView.setText(left + op + right);
            lastType = "num";
        } else {
            left = val;
            leftBtn = b;
            textView.setText(left);
        }
        updateStates();
    }

    private void onOp(Button b) {
        if (okMode || !firstDone || !right.isEmpty()) return;
        op = b.getText().toString();
        textView.setText(left + op);
        lastType = "op";
        updateStates();
    }

    private void onKakutei() {
        if (okMode) {
            if (!lastNumPressed) return;
            //与えられた値と作った値が同じか
            //もしそうなら回数1増やす
            resetNums();
            kakutei.setText("確定");
            okMode = lastNumPressed = false;
            textView.setText("");
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
                        textView.setText(exp + " = エラー");
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

            textView.setText(exp + " = " + resultText);

            leftBtn.setText(resultText);
            rightBtn.setText("");
            rightBtn.setEnabled(false);
            rightBtn.setAlpha(0.4f);

        } catch (Exception e) {
            textView.setText("エラー");
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
        //ここから
        int[] resetint=Creating_Question.createHard();
        int k=0;
        for (Button b : numButtons) {
            b.setText(String.valueOf(resetint[k]));
            k++;
            b.setEnabled(true);
            b.setAlpha(1f);
        }
    }

    private int remainingButtons() {
        int c = 0;
        for (Button b : numButtons)
            if (!b.getText().toString().isEmpty()) c++;
        return c;
    }

    private void disable(Button b) { b.setEnabled(false); b.setAlpha(0.4f); }
    private void enable(Button b, boolean e) { b.setEnabled(e); b.setAlpha(e ? 1f : 0.4f); }
}
