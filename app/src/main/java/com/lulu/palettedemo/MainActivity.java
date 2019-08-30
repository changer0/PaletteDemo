package com.lulu.palettedemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int paletteOptions;

    private int[] coverResIds = new int[]{
            R.mipmap.cover_1,
            R.mipmap.cover_2,
            R.mipmap.cover_3,
            R.mipmap.cover_4,
            R.mipmap.cover_5,
            R.mipmap.cover_6,
            R.mipmap.cover_7,
            R.mipmap.cover_8,
            R.mipmap.cover_9,
            R.mipmap.cover_10,
            R.mipmap.cover_11,
            R.mipmap.cover_12,
            R.mipmap.cover_13,
            R.mipmap.cover_13,
            R.mipmap.cover_14,
            R.mipmap.cover_15,
            R.mipmap.cover_16,
            R.mipmap.cover_17,
            R.mipmap.cover_18,
            R.mipmap.cover_19
    };
    private int curCoverIndex = 0;
    private ImageView ivCover;
    private View showContainer;
    private LinearLayout svControlContainer;
    private TextView tvPaletteInfo;
    private String paletteText;
    private float backAlpha = 1.0f;
    private float gaussAlpha = 1.0f;
    private TextView tvBackAlpha;
    private SeekBar seekBarBackAlpha;
    private ImageView gaussImg;
    private TextView tvGaussAlpha;
    private SeekBar seekBarGaussAlpha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivCover = ((ImageView) findViewById(R.id.iv_cover));
        showContainer = findViewById(R.id.show_container);
        svControlContainer = findViewById(R.id.sv_control_container);
        tvPaletteInfo = findViewById(R.id.palette_info_text);
        tvBackAlpha = findViewById(R.id.back_alpha_info_text);
        tvGaussAlpha = findViewById(R.id.gauss_alpha_info_text);
        seekBarBackAlpha = findViewById(R.id.back_alpha_seek);
        seekBarGaussAlpha = findViewById(R.id.gauss_alpha_seek);
        gaussImg = findViewById(R.id.gauss_img);
        seekBarBackAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                backAlpha = progress/100f;
                tvBackAlpha.setText(progress + "%");
                refreshBackground();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        seekBarGaussAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                gaussAlpha = progress/100f;
                tvGaussAlpha.setText(progress + "%");
                refreshBackground();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curCoverIndex++;
                if (curCoverIndex >= coverResIds.length) {
                    curCoverIndex = 0;
                }
                ivCover.setImageResource(coverResIds[curCoverIndex]);
                refreshBackground();
            }
        });

        findViewById(R.id.previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curCoverIndex--;
                if (curCoverIndex < 0) {
                    curCoverIndex = coverResIds.length - 1;
                }
                ivCover.setImageResource(coverResIds[curCoverIndex]);

                refreshBackground();
            }
        });

        addPaletteOptions();
    }


    /**
     * 添加调色板选项
     */
    private void addPaletteOptions() {
        svControlContainer.addView(generatePaletteButton("VibrantSwatch", 0));
        svControlContainer.addView(generatePaletteButton("LightVibrantSwatch", 1));
        svControlContainer.addView(generatePaletteButton("DarkVibrantSwatch", 2));
        svControlContainer.addView(generatePaletteButton("MutedSwatch", 3));
        svControlContainer.addView(generatePaletteButton("LightMutedSwatch", 4));
        svControlContainer.addView(generatePaletteButton("DarkMutedSwatch", 5));
        refreshBackground();
    }

    private Button generatePaletteButton(final String text, final int options) {
        Button button = new Button(this);
        button.setText(text);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paletteOptions = options;
                refreshBackground();
                paletteText = text;
            }
        });
        return button;
    }

    /**
     * 刷新背景
     */
    private void refreshBackground() {
        Palette.from(getCurBitmap())
                .generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(@Nullable Palette palette) {
                        if (palette == null) {
                            Toast.makeText(MainActivity.this, "palette 为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Palette.Swatch swatch = null;

                        switch (paletteOptions) {
                            case 0:
                                swatch = palette.getVibrantSwatch();//获取到充满活力的这种色调
                                break;
                            case 1:
                                swatch = palette.getLightVibrantSwatch();//获取充满活力的亮
                                break;
                            case 2:
                                swatch = palette.getDarkVibrantSwatch();  //获取充满活力的黑
                                break;
                            case 3:
                                swatch = palette.getMutedSwatch(); //获取柔和的色调
                                break;
                            case 4:
                                swatch = palette.getLightMutedSwatch();//获取柔和的亮
                                break;
                            case 5:
                                swatch = palette.getDarkMutedSwatch(); //获取柔和的黑
                                break;
                        }
                        if (swatch == null) {
                            Toast.makeText(MainActivity.this, "swatch 为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        tvPaletteInfo.setText(paletteText);
                        int rgb = swatch.getRgb();
                        //showContainer.setBackgroundColor(rgb);

                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {
                            getTranslucentColor(0.8f * backAlpha, rgb),
                            getTranslucentColor(1f * backAlpha, rgb),
                            getTranslucentColor(1f * backAlpha, rgb),
                            getTranslucentColor(1f * backAlpha, rgb),
                            getTranslucentColor(1f * backAlpha, rgb),
                            getTranslucentColor(1f * backAlpha, rgb)
                        });
                        showContainer.setBackground(gradientDrawable);
//                        BookDetailBackDrawable background = new BookDetailBackDrawable();
//                        background.setColor(getTranslucentColor(backAlpha, rgb));
                        gaussImg.setImageBitmap(GaussianBlurBitmap.createBlurredBitmap(BitmapFactory.decodeResource(MainActivity.this.getResources(), coverResIds[curCoverIndex])));
                        gaussImg.setAlpha(gaussAlpha);
                        String text = "高斯透明度:" + gaussAlpha + " 背景透明度: " + backAlpha + "\n" +
                                "背景颜色: r:" + Color.red(rgb) + " g:" + Color.green(rgb) + " b:" + Color.blue(rgb);
                        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private Bitmap getCurBitmap() {
        BitmapDrawable drawable = (BitmapDrawable) ivCover.getDrawable();
        return drawable.getBitmap();
    }


    protected int getTranslucentColor(float percent, int rgb) {
        // 10101011110001111
        int blue = Color.blue(rgb);
        int green = Color.green(rgb);
        int red = Color.red(rgb);
        int alpha = Color.alpha(rgb);
//      int blue = rgb & 0xff;
//      int green = rgb>>8 & 0xff;
//      int red = rgb>>16 & 0xff;
//      int backAlpha = rgb>>>24;

        alpha = Math.round(alpha*percent);
        return Color.argb(alpha, red, green, blue);
    }

    @Override
    public void onClick(View v) {

    }
}
