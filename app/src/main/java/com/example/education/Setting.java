package com.example.education;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.education.databinding.ActivitySettingBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Setting extends AppCompatActivity {
    private ActivitySettingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        generateText("text");
        getSupportActionBar().setTitle("ChatGPT");
        Glide.with(this).load(R.drawable.gpt).apply(RequestOptions.circleCropTransform()).into(binding.imageView2);
        binding.btn.setOnClickListener(v -> {
            String text=binding.text.getText().toString().toLowerCase(Locale.ROOT).trim();
            if(TextUtils.isEmpty(text)){
                binding.text.setError("Empty!!");
                binding.text.requestFocus();
            }
            else{
//                final String[] res = {""};
//                        OpenAIChatAPI.sendMessage("Hello, how are you?", new Callback() {
//                            @Override
//                            public void onResponse(@NotNull Call call, @NotNull Response response) {
//                                try {
//                                    res[0] = Objects.requireNonNull(response.body()).string();
//                                    AlertDialog.Builder dialog=new AlertDialog.Builder(Setting.this).setTitle(text).setMessage(res[0]);
//                                    dialog.show();
//
//                                    // Process the response as needed
//                                } catch (Exception e) {
//                                    res[0]=e.getLocalizedMessage();
//                                    AlertDialog.Builder dialog=new AlertDialog.Builder(Setting.this).setTitle(text).setMessage(res[0]);
//                                    dialog.show();
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                                res[0]=e.getLocalizedMessage();
//                                AlertDialog.Builder dialog=new AlertDialog.Builder(Setting.this).setTitle(text).setMessage(res[0]);
//                                dialog.show();
//                            }
//                        });

                generateText(text);
            }
        });
    }
    private void generateText(String message) {
        OkHttpClient client = new OkHttpClient();

        String json = "{\"messages\": [{\"role\": \"system\", \"content\": \"You are ChatGPT, a language model\"}, {\"role\": \"user\", \"content\": \"" + message + "\"}], \"max_tokens\": 50, \"temperature\": 0.7, \"model\": \"gpt-3.5-turbo\"}";
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, mediaType);

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .addHeader("Authorization", "Bearer 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")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    String responseBody = Objects.requireNonNull(response.body()).string();


                    extractResponseContent(responseBody);
//                    Log.d("console",responseBody);
//                    updateUIWithResponse(responseBody);

                    // Extract the response from the JSON and update the UI

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });
    }


    public void extractResponseContent(String responseBody) {

//        Toast.makeText(Setting.this, "bwydbuhb.getString", Toast.LENGTH_SHORT).show();
//        Log.d("console","0");
        try {
//            Log.d("console","1");
            JSONObject jsonObject = new JSONObject(responseBody);
//            Log.d("console","2");

            JSONArray choicesArray = jsonObject.getJSONArray("choices");
//            Log.d("console",choicesArray.toString());

            JSONObject firstChoice = choicesArray.getJSONObject(0);
            JSONObject msg = firstChoice.getJSONObject("message");
//            Log.d("console",msg.toString());

//            Log.d("console",firstChoice.getString("content"));
//            Log.d("console","5");

//            binding.text.setText(firstChoice.getString("content"));
//            Toast.makeText(this, firstChoice.getString("content"), Toast.LENGTH_SHORT).show();
//                                Toast.makeText(Setting.this, firstChoice.getString, Toast.LENGTH_SHORT).show();

            updateUIWithResponse(msg.getString("content"));

        } catch (JSONException e) {
//            Log.d("console","31");
            e.printStackTrace();
        }

    }


    private void updateUIWithResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {



                AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.gpt_dialog, null);
                TextView messageTextView = dialogView.findViewById(R.id.dialog_message);
                builder.setView(dialogView)
                        .setTitle("'"+binding.text.getText().toString().trim()+"'")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
// Handle OK button click
                            }
                        });
                messageTextView.setText(response);
                binding.text.setText("");

                AlertDialog dialog = builder.create();
                dialog.show();

//                outputEditText.setText(response);
            }
        });
    }
}