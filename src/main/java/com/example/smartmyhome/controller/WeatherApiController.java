package com.example.smartmyhome.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class WeatherApiController {

    @GetMapping("/weather")
    public String restApiGetWeather() throws Exception {
        /*자바 현재 날짜 가져오는 함수 */
        // 현재 날짜 구하기
        LocalDateTime now = LocalDateTime.now();
        // 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String hour_t = null;
        String minute_t = null;
        String second_t = null;
        // 년, 월(문자열, 숫자), 일(월 기준, 년 기준), 요일(문자열, 숫자), 시, 분, 초 구하기
        int year = now.getYear(); // 연도
        String month = now.getMonth().toString(); // 월(문자열)
        int monthValue = now.getMonthValue(); // 월(숫자)
        int dayOfMonth = now.getDayOfMonth(); // 일(월 기준)
        int dayOfYear = now.getDayOfYear(); // 일(년 기준)
        String dayOfWeek = now.getDayOfWeek().toString(); // 요일(문자열)
        int dayOfWeekValue = now.getDayOfWeek().getValue(); // 요일(숫자)
        int hour = now.getHour();
        hour_t = Integer.toString(hour);
        int minute = now.getMinute();
        int second = now.getSecond();

        if(minute< 30){
            hour = hour - 1;
            hour_t = Integer.toString(hour);
            if(hour < 0){ // 자정 이전은 전날로 계산해야됨
                now = now.minusDays(1); // 1일전
                dayOfMonth = now.getDayOfMonth();
                monthValue = now.getMonthValue();
                year = now.getYear();
                hour = 23;
                hour_t = Integer.toString(hour);
            }
        }

        if(hour<10) {
            hour_t ='0'+Integer.toString(hour);
        }
        if(minute<10) {
            minute_t ='0'+Integer.toString(minute);
        }
        if(second<10) {
            second_t= '0'+Integer.toString(second);
        }

        String basetime = hour_t +"00";
        formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formatedNow = now.format(formatter);
        /*
            @ API LIST ~
            getUltraSrtNcst 초단기실황조회
            getUltraSrtFcst 초단기예보조회
            getVilageFcst 동네예보조회
            getFcstVersion 예보버전조회
        */
        /*
         * 발표 시각 정리
         *
         * */
        String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst"
                + "?serviceKey=rQ85i%2FmT4znhdli5Nft7VeaiDBs%2FpllPS1Mj4tTTxD9UdA5on5KxaA97uv304ZLJTxBAdlxKXdRQjvQZjW%2Fybw%3D%3D"
                + "&dataType=JSON"            // JSON, XML
                + "&numOfRows=10"             // 페이지 ROWS
                + "&pageNo=1"                 // 페이지 번호
                + "&base_date="+formatedNow       // 발표일자
                + "&base_time="+basetime
                + "&nx=58"                    // 예보지점 X 좌표
                + "&ny=121";                  // 예보지점 Y 좌표
        HashMap<String, Object> resultMap = getDataFromJson(url, "UTF-8", "get", "");
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("data", resultMap);
        return jsonObj.toString();
    }

    @GetMapping("/weather2")
    public String restApiGetWeather2() throws Exception {

        /*자바 현재 날짜 가져오는 함수 */
        // 현재 날짜 구하기
        LocalDateTime now = LocalDateTime.now();
        // 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String hour_t = null;
        String minute_t = null;
        String second_t = null;
        // 년, 월(문자열, 숫자), 일(월 기준, 년 기준), 요일(문자열, 숫자), 시, 분, 초 구하기
        int year = now.getYear(); // 연도
        String month = now.getMonth().toString(); // 월(문자열)
        int monthValue = now.getMonthValue(); // 월(숫자)
        int dayOfMonth = now.getDayOfMonth(); // 일(월 기준)
        int dayOfYear = now.getDayOfYear(); // 일(년 기준)
        String dayOfWeek = now.getDayOfWeek().toString(); // 요일(문자열)
        int dayOfWeekValue = now.getDayOfWeek().getValue(); // 요일(숫자)
        int hour = now.getHour();
        hour_t = Integer.toString(hour);
        int minute = now.getMinute();
        int second = now.getSecond();

        if(minute< 30){
            hour = hour - 1;
            hour_t = Integer.toString(hour);
            if(hour < 0){ // 자정 이전은 전날로 계산해야됨
                now = now.minusDays(1); // 1일전  // https://blog.leocat.kr/notes/2017/07/25/java-add-days 참조
                dayOfMonth = now.getDayOfMonth();
                monthValue = now.getMonthValue();
                year = now.getYear();
                hour = 23;
                hour_t = Integer.toString(hour);
            }
        }

        if(hour<10) {
            hour_t ='0'+Integer.toString(hour);
        }
        if(minute<10) {
            minute_t ='0'+Integer.toString(minute);
        }
        if(second<10) {
            second_t= '0'+Integer.toString(second);
        }

        String basetime = hour_t +"00";
        formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formatedNow = now.format(formatter);
        System.out.println("현재날짜"+ basetime);
        /*
            @ API LIST ~
            getUltraSrtNcst 초단기실황조회
            getUltraSrtFcst 초단기예보조회
            getVilageFcst 동네예보조회
            getFcstVersion 예보버전조회
        */
        /*
         * 발표 시각 정리
         *
         * */

        String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst"
                + "?serviceKey=rQ85i%2FmT4znhdli5Nft7VeaiDBs%2FpllPS1Mj4tTTxD9UdA5on5KxaA97uv304ZLJTxBAdlxKXdRQjvQZjW%2Fybw%3D%3D"
                + "&dataType=JSON"            // JSON, XML
                + "&numOfRows=100"             // 페이지 ROWS
                + "&pageNo=1"                 // 페이지 번호
                + "&base_date="+formatedNow       // 발표일자
                + "&base_time="+basetime           // 발표시각 // 총 몇개인지 조사해야됨 //T1H 기온 C//RN1 1시간 강수량 mm//UUU 동서바람성분 m/s//VVV 남북바람성분 m/s//REH 습도 %// PTY 강수형태 // VEC 풍향 deg// WSD 풍속 m/s
                + "&nx=58"                    // 예보지점 X 좌표
                + "&ny=121";                  // 예보지점 Y 좌표

        HashMap<String, Object> resultMap = getDataFromJson(url, "UTF-8", "get", "");

        System.out.println("# RESULT : " + resultMap);

        JSONObject jsonObj = new JSONObject();

        jsonObj.put("data", resultMap);
        System.out.println(formatedNow); // 2021/06/17
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        return jsonObj.toString();
    }

    @GetMapping("/dust")
    public String restApiGetDust() throws Exception {

        String url = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty"
                +"?serviceKey="+"rQ85i%2FmT4znhdli5Nft7VeaiDBs%2FpllPS1Mj4tTTxD9UdA5on5KxaA97uv304ZLJTxBAdlxKXdRQjvQZjW%2Fybw%3D%3D" // 서비스키
                + "&returnType=json" // 데이터표출방식 json
                + "&numOfRows=100" // 한 페이지 결과 수
                + "&pageNo=1"  // 페이지 번호
                + "&stationName="+"%EB%B3%B8%EC%98%A4%EB%8F%99" // 측정소명
                + "&dataTerm=DAILY" // 데이터 기간 (요청 데이터 기간 <1일 : DAILY, 1개월 : MONTH, 3개월 : 3MONTH > )
                + "&ver=1.0"; // 오퍼레이션 버전

        HashMap<String, Object> resultMap = getDataFromJson(url, "UTF-8", "get", "");

        System.out.println("# RESULT : " + resultMap);

        JSONObject jsonObj = new JSONObject();

        jsonObj.put("data", resultMap);
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        return jsonObj.toString();
    }
    //https://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMinuDustFrcstDspth
    // ?serviceKey=rQ85i%2FmT4znhdli5Nft7VeaiDBs%2FpllPS1Mj4tTTxD9UdA5on5KxaA97uv304ZLJTxBAdlxKXdRQjvQZjW%2Fybw%3D%3D
    // &returnType=json
    // &numOfRows=100
    // &pageNo=1
    // &searchDate=2022-07-02
    // &InformCode=PM10
    // &ver=1.1
    @GetMapping("/dustforecast")
    public String restApiGetDustForecast() throws Exception {
        String url = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMinuDustFrcstDspth"
                +"?serviceKey=rQ85i%2FmT4znhdli5Nft7VeaiDBs%2FpllPS1Mj4tTTxD9UdA5on5KxaA97uv304ZLJTxBAdlxKXdRQjvQZjW%2Fybw%3D%3D"
                +"&returnType=json"
                +"&numOfRows=100"
                +"&pageNo=1"
                +"&searchDate=2022-07-02"
                +"&InformCode=PM10"
                +"&ver=1.1";

        HashMap<String, Object> resultMap = getDataFromJson(url, "UTF-8", "get", "");

        System.out.println("# RESULT : " + resultMap);

        JSONObject jsonObj = new JSONObject();

        jsonObj.put("data", resultMap);
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        return jsonObj.toString();
    }

    public HashMap<String, Object> getDataFromJson(String url, String encoding, String type, String jsonStr) throws Exception {
        boolean isPost = false;
        if ("post".equals(type)) {
            isPost = true;
        } else {
            url = "".equals(jsonStr) ? url : url + "?request=" + jsonStr;
        }

        return getStringFromURL(url, encoding, isPost, jsonStr, "application/json");
    }



    public HashMap<String, Object> getStringFromURL(String url, String encoding, boolean isPost, String parameter, String contentType) throws Exception {
        URL apiURL = new URL(url);
        HttpURLConnection conn = null;
        BufferedReader br = null;
        BufferedWriter bw = null;

        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        try {
            conn = (HttpURLConnection) apiURL.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);

            if (isPost) {
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", contentType);
                conn.setRequestProperty("Accept", "*/*");
            } else {
                conn.setRequestMethod("GET");
            }
            conn.connect();

            if (isPost) {
                bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                bw.write(parameter);
                bw.flush();
                bw = null;
            }
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
            String line = null;
            StringBuffer result = new StringBuffer();
            while ((line=br.readLine()) != null) result.append(line);
            ObjectMapper mapper = new ObjectMapper();
            resultMap = mapper.readValue(result.toString(), HashMap.class);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(url + " interface failed" + e.toString());
        } finally {
            if (conn != null) conn.disconnect();
            if (br != null) br.close();
            if (bw != null) bw.close();
        }
        return resultMap;
    }
}
