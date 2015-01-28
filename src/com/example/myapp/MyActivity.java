package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Arrays;
import java.util.List;

public class MyActivity extends Activity {

    private EditText result;

    private Builder builder = new Builder();

    private Calc.Exp left = Calc.Nothing, right = Calc.Nothing;
    private Calc.BinOpFactory op = Calc.LeftIdentity;
    public enum Pivot { left, right }
    private Pivot pivot;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        result = (EditText) findViewById(R.id.result);

        List<Button> sigils = Arrays.asList(
                (Button) findViewById(R.id.zero),
                (Button) findViewById(R.id.one),
                (Button) findViewById(R.id.two),
                (Button) findViewById(R.id.three),
                (Button) findViewById(R.id.four),
                (Button) findViewById(R.id.five),
                (Button) findViewById(R.id.six),
                (Button) findViewById(R.id.seven),
                (Button) findViewById(R.id.eight),
                (Button) findViewById(R.id.nine));

        for (int i = 0; i < sigils.size(); i++) {
            final int sigil = i;

            Button button = sigils.get(i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buildNumber(sigil);
                }
            });
        }

        Button point = (Button) findViewById(R.id.point);
        point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildDecimalPart();
            }
        });

        List<Button> ops = Arrays.asList(
                (Button) findViewById(R.id.add),
                (Button) findViewById(R.id.sub),
                (Button) findViewById(R.id.mul),
                (Button) findViewById(R.id.div),
                (Button) findViewById(R.id.rem));

        for (final Button button : ops) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buildOp(button);
                }
            });
        }

        Button eval = (Button) findViewById(R.id.eval);
        eval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eval();
            }
        });

        final Button clear = (Button) findViewById(R.id.clear);
        Button clearAll = (Button) findViewById(R.id.clear_all);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear(false);
            }
        });
        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear(true);
            }
        });
    }

    Calc.BinOpFactory op(Button b) {
        switch (b.getId()) {
            case R.id.add: return Calc.Add;
            case R.id.sub: return Calc.Sub;
            case R.id.mul: return Calc.Mul;
            case R.id.div: return Calc.Div;
            case R.id.rem: return Calc.Rem;
        }
        throw new IllegalArgumentException();
    }

    public void buildNumber(int sigil) {
        builder.push(sigil);

        if (pivot == Pivot.right) {
            right = builder.build();
        } else {
            left = builder.build();
        }

        result.setText(op.repr(left, right));
    }

    public void buildDecimalPart() {
        builder.setPoint();
    }

    public void buildOp(Button b) {
        builder.reset();

        if (pivot == Pivot.right) {
            left = op.apply(left, right);
            right = Calc.Nothing;
        } else {
            pivot = Pivot.left;
        }
        op = op(b);

        result.setText(op.repr(left, right));
    }

    public void eval() {
        builder.reset();

        Calc.Exp result = op.apply(left, right).reduce();

        left = result;
        right = Calc.Nothing;
        pivot = Pivot.left;
        op = Calc.LeftIdentity;

        this.result.setText(result.repr());
    }

    public void clear(boolean all) {
        builder.reset();

        if (all) {
            left = right = Calc.Nothing;
            op = Calc.LeftIdentity;
            pivot = Pivot.left;
        } else if (pivot == Pivot.right && right == Calc.Nothing) {
            op = Calc.LeftIdentity;
            pivot = Pivot.left;
        } else if (pivot == Pivot.right) {
            right = Calc.Nothing;
        } else {
            left = Calc.Nothing;
        }

        result.setText(op.repr(left, right));
    }


}
