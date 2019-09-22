package com.example.petong.classattendance;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

public class SearchCourseTask extends AsyncTask<Void, Void, HashMap<String, Object>> {

    final MainActivity.SearchCourseInterface callback;
    User userData;

    public SearchCourseTask(User userData ,MainActivity.SearchCourseInterface callback) {
        this.callback = callback;
        this.userData = userData;
    }

    @Override
    protected HashMap<String, Object> doInBackground(Void...voids) {
        String USER_AGENT = "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36";
        String referrer = "http://www.google.com";
        Map<String, String> cookies = new HashMap<>();

        /*
        try {
            Connection.Response response = Jsoup.connect("https://www3.reg.cmu.ac.th/regist260/public/result.php")
                    .userAgent(USER_AGENT)
                    .timeout(10 * 1000)
                    .method(Connection.Method.POST)
                    .data("id", userData.getData().getStudentCode())
                    //.data("","")
                    .followRedirects(true)
                    .execute();
            cookies = response.cookies();
            Document doc = response.parse();
            //Log.d("Scraping", doc.text());

            HashMap<String, Object> courseList = new HashMap<>();

            Element table = doc.select("table").get(3);
            Elements rows = table.select("tr");
            //Log.d("Scraping",table.text());

            for (int i = 4; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements col = row.select("td");

                if (col.get(0).text().equals("Total Credit")){
                    break;
                }

                Course courseInfo = new Course(col.get(2).text(),
                        col.get(3).text(),
                        col.get(4).text(),
                        col.get(5).text(),
                        col.get(6).text(),
                        col.get(7).text(),
                        col.get(8).text(),
                        col.get(9).text());


                courseList.put(col.get(1).text(), courseInfo);
                //Log.d("Scraping", col.get(1).text() + " " + col.get(2).text());

            }

            return courseList;

        } catch (Exception e){
            e.printStackTrace();
        }*/

        try {
            Map<String, String> data = new HashMap<String, String>();

            Connection.Response loginPageRes = Jsoup.connect("https://oauth.cmu.ac.th/v1/Login.aspx?continue=Registration%20System")
                    .userAgent(USER_AGENT)
                    .timeout(30 * 1000)
                    .method(Connection.Method.GET)
                    .followRedirects(true)
                    .execute();

            data.put("user", "chawit_w");
            data.put("password", "contact00");

            Connection.Response response = Jsoup.connect("https://www1.reg.cmu.ac.th/registrationoffice/student/main.php?mainfile=studentprofile")
                    .method(Connection.Method.POST)
                    .userAgent(USER_AGENT)
                    //.referrer("https://oauth.cmu.ac.th/v1/Login.aspx?continue=Registration%20System")
                    .data(data)
                    .cookies(loginPageRes.cookies())
                    .timeout(30 * 1000)
                    .followRedirects(true)
                    .execute();
            Document doc = response.parse();
            Log.d("Scraping", doc.title());
            Log.d("Scraping", "Hello");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(HashMap<String, Object> courseList) {
        super.onPostExecute(courseList);
        callback.onSearchCourseFinished(courseList);

    }
}
