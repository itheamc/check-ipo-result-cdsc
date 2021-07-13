package com.itheamc.checkiporesult;

import com.itheamc.checkiporesult.models.Company;
import com.itheamc.checkiporesult.utils.ConsoleColor;
import com.itheamc.checkiporesult.utils.Url;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

public class IpoResult {
    private static OkHttpClient okHttpClient;

    public static void main(String[] args) {
        okHttpClient = new OkHttpClient();
        fetchCompany();
    }

    // Function to fetch company lists which results have come
    private static void fetchCompany() {
        List<Company> companies = new ArrayList<>();

        Request request = new Request.Builder().url(Url.COMPANY_LIST_URL).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                notifyErrors(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                    if (jsonObject.getBoolean("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("body");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            Company company = new Company(
                                    jo.getInt("id"),
                                    jo.getString("name"),
                                    jo.getString("scrip"),
                                    jo.getBoolean("isFileUploaded")
                            );
                            companies.add(company);
                        }

                        notifySuccess(companies, null);
                    } else {
                        notifyErrors("Success = false");
                    }

                } catch (JSONException e) {
                    notifyErrors(e.getMessage());
                    e.printStackTrace();
                }
            }
        });

    }


    // Function to check the ipo results
    private static void checkIpoResult(int id, String boid) {
        final MediaType JSON
                = MediaType.get("application/json; charset=utf-8");

        String data = "{ \n" +
                "    \"companyShareId\": \"" + id + "\", \n" +
                "    \"boid\": \"" + boid + "\" \n" +
                "}";
        RequestBody requestBody = RequestBody.create(data, JSON);
        Request request = new Request.Builder().url(Url.IPO_CHECK_URL).post(requestBody).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                notifyErrors(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                    notifySuccess(null, jsonObject);

                } catch (JSONException e) {
                    notifyErrors(e.getMessage());
                    e.printStackTrace();
                }
            }
        });

    }

    private static void notifySuccess(List<Company> companies, JSONObject jsonObject) {
        if (companies != null) {
            formatCompanyData(companies);
        } else {
            String message = jsonObject.getString("message");
            if (message.contains("Sorry")) {
                System.out.println(ConsoleColor.RED_BOLD_BRIGHT + "\n\t______ " + jsonObject.getString("message") + "______" + ConsoleColor.RESET);
            } else {
                System.out.println(ConsoleColor.YELLOW_BOLD_BRIGHT + "\n\t______ " + jsonObject.getString("message") + " ______" + ConsoleColor.RESET);
            }
        }

    }

    private static void notifyErrors(String error) {
        System.out.println(error);
        okHttpClient.dispatcher().cancelAll();
    }

    // Process the company data
    private static void formatCompanyData(@NotNull List<Company> companies) {
        System.out.println(ConsoleColor.WHITE_BOLD_BRIGHT + "\n---------------------------------------------------------------\n\t\t\t----------- CHECK IPO RESULT -----------\n---------------------------------------------------------------" + ConsoleColor.RESET);
        for (Company company : companies) {
            System.out.printf(Locale.ENGLISH, ConsoleColor.CYAN_BOLD + "[+] Enter __ %d __ for " + ConsoleColor.PURPLE_UNDERLINED + "%s%n" + ConsoleColor.RESET, company.getId(), company.getName());
        }

        System.out.print(ConsoleColor.GREEN_BOLD + "\n\n\n[+] Enter Here__   " + ConsoleColor.RESET);
        String id = new Scanner(System.in).nextLine();
        System.out.print(ConsoleColor.GREEN_BOLD + "[+] Enter Your BOID__   " + ConsoleColor.RESET);
        String boid = new Scanner(System.in).nextLine();

        try {
            checkIpoResult(Integer.parseInt(id), boid);
        } catch (NumberFormatException e) {
            notifyErrors(e.getMessage());
            e.printStackTrace();
        }
    }


}

