import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        List<Integer[]> listCount = new ArrayList<>();
        HashMap<Integer, JSONObject> hmJson = new HashMap<>();

        for(int i=0; i<100; i++){
            Connector connector = new Connector("http://codingtest.brique.kr:8080/random");
            Response response = connector.response(Http.Get);

            if(response.getResponseCode()/100 == 2){
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject)jsonParser.parse(response.getData());

                int id = Integer.parseInt(jsonObject.get("id").toString());
                Integer[] tempItem = listCount.stream().filter(item->item[0]==id).findAny().orElse(null);
                if(tempItem != null){
                    tempItem[1]++;
                }
                else{
                    listCount.add(new Integer[] {id, 1});
                    hmJson.put(id, jsonObject);
                }

                listCount = sortList(listCount);
            }
            else{
                System.out.println(response.getResponseCode());
            }
        }

        int totalCount = 0;
        for(Integer[] item : listCount){
            System.out.printf("count: %d %s", item[1], hmJson.get(item[0]).toString());
            System.out.println();

            totalCount += item[1];
        }

        System.out.println("\nTotal count: "+totalCount);
    }

    private static List<Integer[]> sortList(List<Integer[]> list){
        return list.stream().sorted(new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                if(o1[1]<o2[1]){
                    return 1;
                }
                else if(o1[1]>o2[1]){
                    return -1;
                }
                else{
                    return 0;
                }
            }
        }).collect(Collectors.toList());
    }
}
