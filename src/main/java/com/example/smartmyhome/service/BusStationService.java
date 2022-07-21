package com.example.smartmyhome.service;


import com.example.smartmyhome.dto.apidto.BusArrivedDTO;
import com.example.smartmyhome.dto.apidto.BusStationDTO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

@Service
public class BusStationService {


    // GPS좌표를 기반으로 근처(반경 500m)에 있는 정류장을 검색한다.
    public JSONArray getgpsstation(){
        try{
            URL url = new URL("http://apis.data.go.kr/1613000/BusSttnInfoInqireService/getCrdntPrxmtSttnList"
                    + "?serviceKey=rQ85i%2FmT4znhdli5Nft7VeaiDBs%2FpllPS1Mj4tTTxD9UdA5on5KxaA97uv304ZLJTxBAdlxKXdRQjvQZjW%2Fybw%3D%3D"
                    + "&pageNo=1"
                    + "&numOfRows=100"
                    + "&_type=json"
                    + "&gpsLati=37.2952066"
                    + "&gpsLong=126.8615622"
            );
            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8") );
            String result = bf.readLine();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");
            JSONArray jsonArray = (JSONArray) items.get("item");
            return jsonArray;
        }catch (Exception e){
            return null;
        }
    }

    // 리스트 분류
    public ArrayList<BusStationDTO> getgpsstationlist(JSONArray jsonArray){
        ArrayList<BusStationDTO> busStationDTOS = new ArrayList<>();

        for(int i =0 ; i<jsonArray.size(); i++){
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

            String citycode = String.valueOf(jsonObject.get("citycode"));
            Double gpslati = (Double)  jsonObject.get("gpslati");
            Double gpslong = (Double) jsonObject.get("gpslong");
            String nodeid = (String) jsonObject.get("nodeid");
            String nodenm = (String) jsonObject.get("nodenm");
            String nodeno =  String.valueOf(jsonObject.get("nodeno"));

            BusStationDTO busStationDTO =  BusStationDTO.builder()
                    .citycode(citycode)
                    .gpslati(Double.toString(gpslati))
                    .gpslong(Double.toString(gpslong))
                    .nodeid(nodeid)
                    .nodenm(nodenm)
                    .nodeno(nodeno)
                    .build();

            busStationDTOS.add(busStationDTO);
        }

        return busStationDTOS;
    }

    // 정류소 별 도착예정정보목록 조회를 통해서 (도시코드 + nodeid) 별로 전부 조회
    public JSONArray getstationarrive(String citycode, String nodeid) {
        JSONArray jsonArray = null;
        JSONArray item = null;
        JSONObject objitems = null;
        JSONArray arritems = null;
        JSONObject objitem = null;
        JSONArray arritem = null;
        try {
            jsonArray = new JSONArray();
            URL url = new URL("http://apis.data.go.kr/1613000/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList"
                    + "?serviceKey=rQ85i%2FmT4znhdli5Nft7VeaiDBs%2FpllPS1Mj4tTTxD9UdA5on5KxaA97uv304ZLJTxBAdlxKXdRQjvQZjW%2Fybw%3D%3D"
                    + "&pageNo=1"
                    + "&numOfRows=100"
                    + "&_type=json"
                    + "&cityCode=" + citycode  //31090
                    + "&nodeId=" + nodeid //GGB217000002
            );
            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            Object oitems = body.get("items");

            if(oitems instanceof JSONObject ){
                objitems = (JSONObject) oitems;
            }else if(oitems instanceof JSONArray){
                arritems = (JSONArray) oitems;
            }else{
                return jsonArray;
            }

            Object oitem = objitems.get("item");
            if(oitem instanceof JSONObject){
                objitem = (JSONObject) oitem;
                jsonArray.add(objitem);
                return jsonArray;

            }else if(oitem instanceof JSONArray){
                arritem = (JSONArray) oitem;
            }

            return arritem;
        } catch (Exception e) {
            return arritem;
        }
    }

    // 리스트 분류
    public ArrayList<BusArrivedDTO> getstationarrivelist(JSONArray jsonArray){
        ArrayList<BusArrivedDTO> busArrivedDTOS = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();

        for(int i =0 ; i<jsonArray.size(); i++){
            if(jsonArray.get(i) == null ){
                return busArrivedDTOS;
            }else{
                jsonObject = (JSONObject) jsonArray.get(i);
                String arrprevstationcnt = String.valueOf(jsonObject.get("arrprevstationcnt"));
                String arrtime = String.valueOf(jsonObject.get("arrtime"));
                String nodeid = (String) jsonObject.get("nodeid");
                String nodenm = (String) jsonObject.get("nodenm");
                String routeid = (String) jsonObject.get("routeid");
                String routeno = String.valueOf(jsonObject.get("routeno"));
                String routetp = (String) jsonObject.get("routetp");
                String vehicletp = (String) jsonObject.get("vehicletp");

                BusArrivedDTO busArrivedDTO =  BusArrivedDTO.builder()
                        .arrprevstationcnt(arrprevstationcnt)
                        .arrtime(arrtime)
                        .nodeid(nodeid)
                        .nodenm(nodenm)
                        .routeid(routeid)
                        .routeno(routeno)
                        .routetp(routetp)
                        .vehicletp(vehicletp)
                        .build();
                busArrivedDTOS.add(busArrivedDTO);
            }
        }
        return busArrivedDTOS;
    }
}
