import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.tqmall.monkey.component.utils.HttpClientUtil;
import com.tqmall.monkey.component.utils.Sha1Util;
import org.junit.Test;
import redis.clients.util.JedisURIHelper;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ximeng on 2015/4/30.
 */
public class CommonTest {
    @Test
    public void testkeyGoods(){
        String url = "http://121.199.57.39:8087/sellersearch/goods/list?sellerId=1&key=刹车片&idNotIn=333444&start=0&limit=5";
        String json = HttpClientUtil.getUrl(url).getResult();
        String data = JSON.parseObject(json).getString("data");
        boolean success = JSON.parseObject(json).getBoolean("success");

        System.out.print(success);
    }

    @Test
    public void testTime(){
        Date now = new Date();
        Calendar cal = Calendar.getInstance();

        DateFormat d1 = DateFormat.getDateInstance(); //默认语言（汉语）下的默认风格（MEDIUM风格，比如：2008-6-16 20:54:53）
        String str1 = d1.format(now);
        DateFormat d2 = DateFormat.getDateTimeInstance();
        String str2 = d2.format(now);
        DateFormat d3 = DateFormat.getTimeInstance();
        String str3 = d3.format(now);
        DateFormat d4 = DateFormat.getInstance(); //使用SHORT风格显示日期和时间
        String str4 = d4.format(now);

        DateFormat d5 = DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.FULL); //显示日期，周，时间（精确到秒）
        String str5 = d5.format(now);
        DateFormat d6 = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG); //显示日期。时间（精确到秒）
        String str6 = d6.format(now);
        DateFormat d7 = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT); //显示日期，时间（精确到分）
        String str7 = d7.format(now);
        DateFormat d8 = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM); //显示日期，时间（精确到分）
        String str8 = d8.format(now);//与SHORT风格相比，这种方式最好用





        System.out.println("用Date方式显示时间: " + now);//此方法显示的结果和Calendar.getInstance().getTime()一样


        System.out.println("用DateFormat.getDateInstance()格式化时间后为：" + str1);
        System.out.println("用DateFormat.getDateTimeInstance()格式化时间后为：" + str2);
        System.out.println("用DateFormat.getTimeInstance()格式化时间后为：" + str3);
        System.out.println("用DateFormat.getInstance()格式化时间后为：" + str4);

        System.out.println("用DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.FULL)格式化时间后为：" + str5);
        System.out.println("用DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG)格式化时间后为：" + str6);
        System.out.println("用DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT)格式化时间后为：" + str7);
        System.out.println("用DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM)格式化时间后为：" + str8);
    }
    @Test
    public void testERP(){
        String goodsId = "1184";
        Integer limit = 1;
        Map<String,Object> map = new HashMap<>();
        map.put("goodsId", goodsId);
        map.put("limit", limit);

        String appId = "782629";
        String signkey = "54385fdvjhurn84r";

        String sign = Sha1Util.ERPSha(appId,signkey,map);

        String url = "http://crm.tqmall.com/saint/openInterface/erp/purchase/query_recent_bill_4_whole?";
        url += "goodsId="+goodsId;
        url += "&appId="+appId;
        url += "&sign="+sign;
        url += "&limit="+limit;

        String json = HttpClientUtil.getUrl(url).getResult();
        System.out.println(json);

    }

    @Test
    public void testSearch(){
        String searchHost = "http://112.124.11.106:8080/";
        String OE = "45137AE002";

        String url = searchHost + "elasticsearch/goods/sample_car_parts?oeNum="+OE;
        String json = HttpClientUtil.getUrl(url).getResult();

        JSONObject object = JSON.parseObject(json);
        JSONObject response = object.getJSONObject("response");
        JSONArray list = response.getJSONArray("list");

        JSONObject result = list.getJSONObject(0);
        Integer goodsId = result.getInteger("id");

        System.out.print(goodsId);
    }

    @Test
    public void testLongToDate(){
        long time = Long.parseLong("1429201685000");
        java.util.Date dt = new Date(time);
        System.out.println(dt.toString());
    }

    @Test
    public void testSpilt(){
        String displacement = "";
        List<String> displaceList = Arrays.asList(displacement.split("/"));
//        System.out.print(displaceList.size());
        for(String a:displaceList){
            System.out.println(a);
        }
        String[] year_array = displacement.split("/");
        for(String b : year_array){
            System.out.println(b);
        }
//        String q = "2011231";
//        String b = q.substring(0,4);
//        System.out.print(b);
//        OfferGoodsDO offerGoodsDO = new OfferGoodsDO();
//        offerGoodsDO.setFirstCateName(null);
//        String[] result = q.split("-");
//        System.out.print("start===:"+result[0]+"====:"+result[1]+"===end");
    }

    @Test
    public void testBigDecimal(){
        BigDecimal a = new BigDecimal("1");
        BigDecimal b = new BigDecimal("2");

        System.out.print(a.compareTo(b));
    }



    @Test
    public void tesetURI(){
        String host = "redis://127.0.0.1:6379";
        URI uri = URI.create(host);
        if(JedisURIHelper.isValid(uri)) {
            String host1 = uri.getHost();
            int port = uri.getPort();
            String password = JedisURIHelper.getPassword(uri);
            int db = JedisURIHelper.getDBIndex(uri);

            System.out.println("host:"+host1+" port:" + port + " password:" + password);
            System.out.println(" db:"+db);
        }
    }

    @Test
    public void testFinal(){
        String a = "hello2";
        final String b = "hello";
        String d = "hello";
        String c = b + 2;
        String e = d + 2;
        System.out.println((a == c));
        System.out.println((a == e));
    }

    //分割
    @Test
    public void testSubString(){
        String s = "A1";
        String a,b;
        a=s.substring(2); //截取掉s从首字母起长度为begin的字符串，将剩余字符串赋值给s；
        b=s.substring(0,2);//截取s中从begin开始至end结束时的字符串，并将其赋值给s;
        System.out.println("a="+a+" b="+b);
//        System.out.print("start===:"+result[0]+"====:"+result[1]+"===end");
    }

    //正则
    @Test
    public void testMatcher(){
        Pattern pattern = Pattern.compile("[^\\w]");
//        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher matcher = pattern.matcher("J9121S$2A");
        boolean b= matcher.matches();
        //当条件满足时，将返回true，否则返回false
        System.out.println(matcher.find());
//        System.out.print("start===:"+result[0]+"====:"+result[1]+"===end");
    }

    //replacceAll
    @Test
    public void testReplacceAll(){
        String a = "asd sd  sds  dsad ";
        a = a.replaceAll(" +","");
        System.out.print(a);

    }
    //url encode
    @Test
    public void testEncode(){
        String searchHost = "http://search.tqmall.com/elasticsearch/";
        String format = "0986AF0029";
        String brandName = "博世";
        try {
            format = URLEncoder.encode(format, "UTF-8");
            brandName = URLEncoder.encode(brandName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = searchHost + "goods/convert?q=*&brandName="+brandName+"&goodsFormat="+format;
        System.out.println(url);
        String json = HttpClientUtil.getUrl(url).getResult();
        JSONObject object = JSON.parseObject(json);
        JSONObject response = object.getJSONObject("response");
        JSONArray list = response.getJSONArray("list");

        System.out.println(list.size());


    }

    @Test
    public void testHashMap(){
        Multimap<String, String> myMultimap = ArrayListMultimap.create();

        // 添加键值对
        myMultimap.put("Fruits", "Bannana");
        //给Fruits元素添加另一个元素
        myMultimap.put("Fruits", "Apple");
        myMultimap.put("Fruits", "Pear");
        myMultimap.put("Fruits", "Pear");
        Collection<String> fruits = myMultimap.get("Fruits");
        Set<String> resultList = Sets.newHashSet(fruits);

        for(String result : resultList) {
            System.out.println(result); // [Bannana, Apple, Pear]
        }

        Collection<String> test = myMultimap.get("test");

        System.out.print("hh");

    }

    @Test
    public void testSha(){
        String pass = "test123";
        System.out.println(Sha1Util.getSha1(pass));
    }


    @Test
    public void treeSet(){
        TreeSet hashSet = new TreeSet();

        hashSet.add("CBH0816A0008"); //向集合中添加一个字符串
        hashSet.add("CBH0816A0001");
        hashSet.add("CBH0816A0012");
        hashSet.add("CBH0816A0008");
        hashSet.add("CBH0816A1000");
        hashSet.add("CBH0816A0008");
        hashSet.add("CBH0816A0020");


        Iterator it = hashSet.iterator();
        while(it.hasNext()){
            System.out.println(it.next()+",");
        }
    }

    @Test
    public void testSubstring(){
        String sumCode = "40101000";
        String firstCode = sumCode.substring(0, 1);
        System.out.println("firstCode = " + firstCode);
        String secondCode = sumCode.substring(1, 3);
        System.out.println("secondCode = " + secondCode);
        String thirdCode = sumCode.substring(3, 6);
        System.out.println("thirdCode = " + thirdCode);
    }


    @Test
    public void testCompare(){
        String code1 = "40101000";
        String code2 = "40101010";
        String code3 = "40100000";

        System.out.println(""+code1.compareToIgnoreCase(code2));
        System.out.println(""+code1.compareToIgnoreCase(code3));

    }


    @Test
    public void testMap(){
        HashMap<String, String> keySetMap = new HashMap<String, String>();
        HashMap<String, String> entrySetMap = new HashMap<String, String>();

        for (int i = 0; i < 100000; i++) {
            keySetMap.put("" + i, "keySet");
        }
        for (int i = 0; i < 100000; i++) {
            entrySetMap.put("" + i, "entrySet");
        }

        long startTimeOne = System.currentTimeMillis();
        Iterator<String> keySetIterator = keySetMap.keySet().iterator();
        while (keySetIterator.hasNext()) {
            String key = keySetIterator.next();
            String value = keySetMap.get(key);
//            System.out.println(value);
        }

        System.out.println("keyset spent times:"
                + (System.currentTimeMillis() - startTimeOne));

        long startTimeTwo = System.currentTimeMillis();

        Iterator<Map.Entry<String, String>> entryKeyIterator = entrySetMap
                .entrySet().iterator();
        while (entryKeyIterator.hasNext()) {
            Map.Entry<String, String> e = entryKeyIterator.next();
//            System.out.println(e.getValue());
        }
        System.out.println("entrySet spent times:"
                + (System.currentTimeMillis() - startTimeTwo));
    }



    @Test
    public void testFile(){
        String base = "/Users/zxg/connectio.txt";


        File file = new File(base);
        if(!file.exists()){
            file.mkdirs();
        }


    }


    @Test
    public void replace(){
        String a = "2009.08.10-2012.08.31，“I30\"";
        String b = a.replace("“","\"").replace("”","\"")
                .replace("\"","'");
        System.out.printf(b);
    }

    @Test
    public void testSetSort(){
        Set<String> testSet = new HashSet<>();
        testSet.add("G");
        testSet.add("A");
        testSet.add("V");
        testSet.add("B");
        testSet.add("G");

        System.out.println(testSet.toString());

        List<String> a = new ArrayList<>(testSet);
        Collections.sort(a, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        System.out.println(a.toString());
    }


    @Test
    public void testReg(){
        String oe_num = "1#2#3?1";
        String oe_re = oe_num.replace("#","");

        String specialEx = "[^\\w]";
        Pattern specialPat = Pattern.compile(specialEx);
        Matcher specialMatcher = specialPat.matcher(oe_re);
        if (specialMatcher.find()) {
            System.out.println("含特殊字符");
        }else{
            System.out.println("不含特殊字符");
        }
    }

    @Test
    public void testString(){
        String test = "hello world you~";
        for(char a : test.toCharArray()){
            String b = String.valueOf(a);
            System.out.println(b);
        }
    }

    @Test
    public void testIp() throws UnknownHostException {
        String ip = InetAddress.getLocalHost().getHostAddress();
        System.out.println("===============:"+ip);
    }
}
