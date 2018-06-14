package com.bloomers.tedxportsaid.Utitltes;


import android.util.Base64;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FaviconParser {
    //wrapper around Java's primitives and objects
    //Allow implement C-like "out" filling
    public class SimpleWrap<T> {
        private T value;

        public SimpleWrap(T v) {
            value = v;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T v) {
            value = v;
        }
    }
    //Callback's declaration
    public interface FaviconParserCallback {
        //Attempts to save icon; return True if icon accepted and handled
        //client should self check icon format and sizes. Because all Web Developers born in India:)
        boolean onSaveIcon(String srcUrl, String iconUrl, byte[] iconData);
        //load data from web; NOTE: client should handle redirects and errors; in outUrl should be passed final URL after connection
        void loadFromWebByUrl(String srcUrl, SimpleWrap<String> outUrl, SimpleWrap<byte[]> outData);
        //service method to handle results
        void onFinishResult(String srcUrl, int resultCode);
        //native implementation for resolving URL's
        void resolveURL(String baseUrl, String relativeURl, SimpleWrap<String> outUrl);
        void logging(String msg);
    }



    //result codes
    public static final int RESULT_OK = 0;
    public static final int RESULT_NOT_FOUND_FOR_THIS_LIMITS = 1;
    public static final int RESULT_URL_NOT_EXIST = 2;
    public static final int RESULT_NOT_FOUND_FOR_THIS_URL = 3;
    public static final int RESULT_UNEXPECTED_ERROR = 4;



    /**************************** Docs ****************************/

    // FAVICON DOCS:
    // http://www.jonathantneal.com/blog/understand-the-favicon/
    // http://olegorestov.ru/html5/favicon/
    // http://habrahabr.ru/company/ifree/blog/216045/
    //https://developer.chrome.com/apps/manifest/icons

    // ICON SIZE DOCS:
    // https://developer.apple.com/library/ios/documentation/UserExperience/Conceptual/MobileHIG/IconMatrix.html
    // http://msdn.microsoft.com/en-us/library/ie/dn255024(v=vs.85).aspx
    // http://www.aha-soft.com/faq/android-icons-images.htm

    // data in HREF may have values:
    // absolute path - 'http://site.com/img/icon.ico'
    // relative '../../../icon.ico'
    // './icon.ico'
    // '/icon.ico'
    // 'icon.ico'
    // base64 encoded icon 'data:image/png;base64,123456=='

    // https://thebc.co/website-design/design-implement-favicon/
    // Favicon Size List

    // 16 x 16 Standard size for browsers
    // 24 x 24 IE9 pinned site size for user interface
    // 32 x 32 IE new page tab, Windows 7+ taskbar button, Safari Reading List
    // sidebar
    // 48 x 48 Windows site
    // 57 x 57 iPod touch, iPhone up to 3G
    // 60 x 60 iPhone touch up to iOS7
    // 64 x 64 Windows site, Safari Reader List sidebar in HiDPI/Retina
    // 70 x 70 Win 8.1 Metro tile
    // 72 x 72 iPad touch up to iOS6
    // 76 x 76 iOS7
    // 96 x 96 GoogleTV
    // 114 x 114 iPhone retina touch up to iOS6
    // 120 x 120 iPhone retina touch iOS7
    // 128 x 128 Chrome Web Store app, Android
    // 144 x 144 IE10 Metro tile for pinned site, iPad retina up to iOS6
    // 150 x 150 Win 8.1 Metro tile
    // 152 x 152 iPad retina touch iOS7
    // 196 x 196 Android Chrome
    // 310 x 150 Win 8.1 wide Metro tile
    // 310 x 310 Win 8.1 Metro tile


    /**************************** constants ****************************/
    // MS WIN8
    final String[] msTags = new String[]{
         "\"msapplication-square70x70logo\"",
         "\"msapplication-TileImage\"",
         "\"msapplication-square150x150logo\"",
         "\"msapplication-square310x310logo\""};
    // tags full list
    final String[] iconTagsFull = new String[]{
         // Firefox, Opera
         "\"icon\"",
         // Chrome, Safari, IE
         "\"shortcut icon\"",
         // iOS/Android
         "\"apple-touch-icon"// == apple-touch-iconXXXX
         //"\"apple-touch-icon\"",
         // "\"apple-touch-icon-precomposed\"",
         //"\"apple-touch-startup-image\""
    };

    private static final int maxPossibleIconSize = 512;
    private static final double defaultAcceptedDifference = 0.5d;

    /**************************** Implementation ****************************/


    private FaviconParserCallback callback;
    private String targetUrl;

    public String startFaviconSearch(Document document,String targetUrl) {
        callback= new FaviconParserCallback() {
            @Override
            public boolean onSaveIcon(String srcUrl, String iconUrl, byte[] iconData) {
                return false;
            }

            @Override
            public void loadFromWebByUrl(String srcUrl, SimpleWrap<String> outUrl, SimpleWrap<byte[]> outData) {

            }

            @Override
            public void onFinishResult(String srcUrl, int resultCode) {

            }

            @Override
            public void resolveURL(String baseUrl, String relativeURl, SimpleWrap<String> outUrl) {

            }

            @Override
            public void logging(String msg) {

            }
        };
        return  startFaviconSearch(document,targetUrl, 32);
    }

    //Force the loading of any icon for the target link;
    //Trying to find the nearest to "desiredSize" and +-50% of the accepted sizes, icons;
    //In case the preferred one is not found - will return any other icon, if possible. Otherwise return code RESULT_NOT_FOUND_FOR_THIS_URL
    public String startFaviconSearch(Document document,String targetUrl, int desiredSize) {
      return startFaviconSearch(document,targetUrl, desiredSize, defaultAcceptedDifference, callback);
    }


    //Force the loading of any icon for the target link;
    //Trying to find the nearest to 32px and +-50% of the accepted sizes, icons;
    //In case the preferred one is not found - will return any other icon, if possible. Otherwise return code RESULT_NOT_FOUND_FOR_THIS_URL

    //Force the loading of any icon for the target link;
    //Trying to find the nearest to "desiredSize" and +-"acceptedDifference" of the accepted sizes, icons;
    //In case the preferred one is not found - will return any other icon, if possible. Otherwise return code RESULT_NOT_FOUND_FOR_THIS_URL
    public String startFaviconSearch(Document document,String targetUrl, int desiredSize, double acceptedDifference, FaviconParserCallback callback) {
       return startFaviconSearch(document,targetUrl, desiredSize, acceptedDifference, true, callback);
    }

    // Attempts to load an icon for the specified URL, with the specified size and size limits.
    //  If the flag "force" is set to True - will always try to find the icon.
    //  If the flag "force" is set to False, if nothing is found by the specified parameters, return code RESULT_NOT_FOUND_FOR_THIS_LIMITS or RESULT_NOT_FOUND_FOR_THIS_URL if no icons for this site
    public String startFaviconSearch(Document document,String targetUrl, int desiredSize, double acceptedDifference, boolean forcedResult, FaviconParserCallback callback) {
        this.targetUrl = targetUrl;
        this.callback = callback;

        SimpleWrap<String> outUrl = new SimpleWrap<>(targetUrl);
        SimpleWrap<byte[]> outData = new SimpleWrap<>(new byte[0]);

        //load html
        this.callback.loadFromWebByUrl(this.targetUrl, outUrl, outData);

        byte[] data = outData.getValue();
        String actualUrl = outUrl.getValue();
        //check is html data not empty

       return parse(actualUrl,  document.html(), desiredSize, acceptedDifference, forcedResult);



    }

    //parse HTML (head and meta) and Manifest.json
    public String parse(String srcUrl, String html, int desiredSize, double acceptedDifference, boolean forcedResult) {
        int minSize = desiredSize - (int) (desiredSize * acceptedDifference);//calculate min accepted size
        int maxSize = desiredSize + (int) (desiredSize * acceptedDifference);//calculate max accepted size
        int resultCounter = 0;
        callback.logging("start parsing, srcUrl "+srcUrl +", minsize: "+minSize+", maxsize: "+maxSize+", forced: "+forcedResult);

        Pattern p = Pattern.compile("<link(.*?)>"); //compile RegEx to extract all <link/> tags
        Matcher m = p.matcher(html.replace('\'', '"'));
        //enumerate matches
        while (m.find()) {
            String rel = m.group(1);
            if (rel.contains("\"manifest\"")) { //special case for Manifest.json
                callback.logging("manifest detected: "+rel);

                resultCounter = resultCounter + parseManifest(rel, srcUrl, minSize, maxSize, forcedResult);
            } else
                for (String iconTag : iconTagsFull) { //looks for standard tags
                    if (rel.contains(iconTag)) {
                        callback.logging("standard tags detected: "+rel);
                        String result = parseRel(rel, srcUrl, minSize, maxSize, forcedResult);//ok, found one, attempts to parse
                        if (!isEmpty(result) && (int)handleIconUrl(result).first == RESULT_OK)//if result not null, attempts to save icon and increment counter
                            resultCounter++;
                        return (String) handleIconUrl(result).second;

                    }
                }


        }


        //same with <meta> tag. MS, I love you. Actually no :)
        p = Pattern.compile("<meta(.*?)>");
        m = p.matcher(html.replace('\'', '"'));
        while (m.find()) {
            String rel = m.group(1);
            for (String iconTag : msTags) {
                if (rel.contains(iconTag)) {
                    callback.logging("MS tags detected: "+rel);
                    String result = parseMeta(rel, srcUrl, minSize, maxSize, forcedResult);
                    if (!isEmpty(result) && (int)handleIconUrl(result).first == RESULT_OK)
                        resultCounter++;
                    return (String) handleIconUrl(result).second;
                }

            }


        }


        //don't forget about default favicon.ico
        if (handleDefaultFavicon(srcUrl, forcedResult, minSize, maxSize)) {
            resultCounter++;
        }


        //send result to client
        if (resultCounter == 0) {
            if (!forcedResult)
                this.callback.onFinishResult(targetUrl, RESULT_NOT_FOUND_FOR_THIS_LIMITS);
            else
                this.callback.onFinishResult(targetUrl, RESULT_NOT_FOUND_FOR_THIS_URL);
        } else
            this.callback.onFinishResult(targetUrl, RESULT_OK);

        return "";

    }

    //Attempts to save icon by provided URL
    private Pair handleIconUrl(String iconUrl) {
        callback.logging("Attempts to save icon: "+iconUrl);
        byte[] data = null;

        // base64 encoded into HTML
        if (iconUrl.startsWith("data:")) {
            try {
                String base64 = iconUrl.substring(iconUrl.indexOf("base64")
                     + "base64".length() + 1, iconUrl.length());
                data = Base64.decode(base64, Base64.DEFAULT);
            } catch (Exception e){
                e.printStackTrace();
            }
        } else {
            //load from web
            SimpleWrap<String> outUrl = new SimpleWrap<>(iconUrl);
            SimpleWrap<byte[]> outData = new SimpleWrap<>(new byte[0]);
            this.callback.loadFromWebByUrl(iconUrl, outUrl, outData);
            data = outData.getValue();
            iconUrl = outUrl.getValue();
        }




        if (data != null && data.length != 0) {
            //Attempts to save;
            //if client return FALSE, that means that icon was not accepted for some reason ¯\_(ツ)_/¯
            if (this.callback.onSaveIcon(this.targetUrl, iconUrl, data))
                return new Pair(RESULT_OK,iconUrl);
            else
                return new Pair(RESULT_UNEXPECTED_ERROR,iconUrl) ;//cann't save icon, wrong format ect.
        }

        return new Pair(RESULT_NOT_FOUND_FOR_THIS_URL,iconUrl) ;
    }

    //workaround for default "scheme://host/favicon.ico"
    private boolean handleDefaultFavicon(String srcUrl, boolean forcedResult, int minSize, int maxSize) {
        boolean forceLoad = forcedResult;
        callback.logging("Default favicon");
        //for case if forceLoad disabled
        //check limits
        //Yes yes, I know. ICO can contain few icon sizes
        if (!forceLoad)
            forceLoad = (minSize <= 16 && 16 <= maxSize);
        if (!forceLoad)
            forceLoad = (minSize <= 24 && 24 <= maxSize);
        if (!forceLoad)
            forceLoad = (minSize <= 32 && 32 <= maxSize);
        if (!forceLoad)
            forceLoad = (minSize <= 48 && 48 <= maxSize);

        if (forceLoad) {
            int end = srcUrl.indexOf("/", srcUrl.indexOf("://") + 3);
            if (end == -1)
                end = srcUrl.length();

            String parentUrl = srcUrl.substring(0, end);
            String favicon = parentUrl + "/favicon.ico";
            return (int) handleIconUrl(favicon).first == RESULT_OK;
        }
        return false;
    }


    //parse REL
    private String parseRel(String rel, String srcUrl, int minSize, int maxSize, boolean forced) {

        rel = rel.replace('\'', '"').toLowerCase();

        //compile RegEx to find all links
        Pattern p = Pattern.compile("href=\"(.*?)\"");

        Matcher m = p.matcher(rel);

        final String parentScheme = srcUrl.substring(0, srcUrl.indexOf("://"));

        //enumerate
        while (m.find()) {

            String link = m.group(1);

            if (isEmpty(link))
                continue;

            if (link.startsWith("//"))
                link = parentScheme + ":" + link;

            if (!link.startsWith("http://") && !link.startsWith("https://"))
                link = absoluteURL(srcUrl, link);

            if (rel.contains("sizes=")) {//Woohoo!! Something found
                //magic
                int mediumSize = (maxSize + minSize) / 2;
                int toLow = mediumSize;
                int toUp = mediumSize;
                //soft search, equals "no forced"
                for (int i = mediumSize; i <= maxPossibleIconSize; i++) {
                    if (toLow > minSize)
                        toLow--;
                    if (toUp < maxSize)
                        toUp++;
                    if (toLow == minSize && toUp == maxSize)
                        break;
                    if (toLow >= 0 && rel.contains("sizes=\"" + toLow + "x" + toLow + "\"")) {
                        callback.logging("size: "+"sizes=\"" + toLow + "x" + toLow + "\"" +", link: "+link);
                        return link;
                    }
                    if (toLow <= maxPossibleIconSize && rel.contains("sizes=\"" + toUp + "x" + toUp + "\"")) {
                        callback.logging("size: "+"sizes=\"" + toUp + "x" + toUp + "\"" +", link: "+link);
                        return link;
                    }

                }


                //if forced, we should return something
                if (forced) {
                    for (int i = mediumSize; i <= maxPossibleIconSize; i++) {

                        if (toLow > 0)
                            toLow--;
                        if (toUp < maxPossibleIconSize)
                            toUp++;
                        if (toLow == 0 && toUp == maxPossibleIconSize + 1)
                            break;
                        if (toLow >= 0 && rel.contains("sizes=\"" + toLow + "x" + toLow + "\"")) {
                            callback.logging("size: "+"sizes=\"" + toLow + "x" + toLow + "\"" +", link: "+link);
                            return link;
                        }

                        if (toLow <= maxPossibleIconSize && rel.contains("sizes=\"" + toUp + "x" + toUp + "\""))
                        {
                            callback.logging("size: "+"sizes=\"" + toUp + "x" + toUp + "\"" +", link: "+link);
                            return link;
                        }


                    }
                }

            } else {//Hmmmm, in case "sizes" attribute missed, lets decide that icon same as default icon (see handleDefaultFavicon())
                boolean forceLoad = forced;
                if (!forceLoad)
                    forceLoad = (minSize <= 16 && 16 <= maxSize);
                if (!forceLoad)
                    forceLoad = (minSize <= 24 && 24 <= maxSize);
                if (!forceLoad)
                    forceLoad = (minSize <= 32 && 32 <= maxSize);
                if (!forceLoad)
                    forceLoad = (minSize <= 48 && 48 <= maxSize);

                if (forceLoad)
                {
                    callback.logging("size: Unknown, link: "+link);
                    return link;
                }
            }
            ///

        }


        return null;
    }


    //same for META
    private String parseMeta(String rel, String srcUrl, int minSize, int maxSize, boolean forced) {

        rel = rel.replace('\'', '"').toLowerCase();
        // need for conversation
        // name="msapplication-square70x70logo" -> sizes="70x70"
        String[] msTagsHack =
             {        "sizes=\"70x70\"",
                  "sizes=\"144x144\"",
                  "sizes=\"150x150\"",
                  "sizes=\"310x310\""};

        for (int i = 0; i < msTags.length; i++)
            rel = rel.replace("name=" + msTags[i], msTagsHack[i]);

        callback.logging("parseMeta: "+rel);

        Pattern p = Pattern.compile("content=\"(.*?)\"");
        Matcher m = p.matcher(rel);
        final String parentScheme = srcUrl.substring(0, srcUrl.indexOf("://"));
        while (m.find()) {

            String link = m.group(1);

            if (isEmpty(link))
                continue;

            if (link.startsWith("//"))
                link = parentScheme + ":" + link;

            if (!link.startsWith("http://") && !link.startsWith("https://"))
                link = absoluteURL(srcUrl, link);

            if (rel.contains("sizes=")) {
                //magic
                int mediumSize = (maxSize + minSize) / 2;
                int toLow = mediumSize;
                int toUp = mediumSize;
                //soft search, equls "no forced"
                for (int i = mediumSize; i <= maxPossibleIconSize; i++) {
                    if (toLow > minSize)
                        toLow--;
                    if (toUp < maxSize)
                        toUp++;
                    if (toLow == minSize && toUp == maxSize)
                        break;
                    if (toLow >= 0 && rel.contains("sizes=\"" + toLow + "x" + toLow + "\""))
                    {
                        callback.logging("size: "+"sizes=\"" + toLow + "x" + toLow + "\"" +", link: "+link);
                        return link;
                    }
                    if (toLow <= maxPossibleIconSize && rel.contains("sizes=\"" + toUp + "x" + toUp + "\""))
                    {
                        callback.logging("size: "+"sizes=\"" + toUp + "x" + toUp + "\"" +", link: "+link);
                        return link;
                    }

                }

                if (forced) {
                    for (int i = mediumSize; i <= maxPossibleIconSize; i++) {

                        if (toLow > 0)
                            toLow--;
                        if (toUp < maxPossibleIconSize)
                            toUp++;
                        if (toLow == 0 && toUp == maxPossibleIconSize + 1)
                            break;
                        if (toLow >= 0 && rel.contains("sizes=\"" + toLow + "x" + toLow + "\""))
                        {
                            callback.logging("size: "+"sizes=\"" + toLow + "x" + toLow + "\"" +", link: "+link);
                            return link;
                        }

                        if (toLow <= maxPossibleIconSize && rel.contains("sizes=\"" + toUp + "x" + toUp + "\""))
                        {
                            callback.logging("size: "+"sizes=\"" + toUp + "x" + toUp + "\"" +", link: "+link);
                            return link;
                        }


                    }
                }


            } else {
                boolean forceLoad = forced;
                if (!forceLoad)
                    forceLoad = (minSize <= 16 && 16 <= maxSize);
                if (!forceLoad)
                    forceLoad = (minSize <= 24 && 24 <= maxSize);
                if (!forceLoad)
                    forceLoad = (minSize <= 32 && 32 <= maxSize);
                if (!forceLoad)
                    forceLoad = (minSize <= 48 && 48 <= maxSize);

                if (forceLoad)
                {
                    callback.logging("size: Unknown, link: "+link);
                    return link;
                }
            }
            ///
        }

        return null;
    }

    //special case, Manifest.json parsing
    private int parseManifest(String rel, String srcUrl, int minSize, int maxSize, boolean forced) {
        int resultCounter = 0;
        try {

            rel = rel.replace('\'', '"').toLowerCase();

            Pattern p = Pattern.compile("href=\"(.*?)\"");

            Matcher m = p.matcher(rel);

            final String parentScheme = srcUrl.substring(0, srcUrl.indexOf("://"));
            while (m.find()) {

                String link = m.group(1);

                if (isEmpty(link))
                    continue;

                if (link.startsWith("//"))
                    link = parentScheme + ":" + link;

                if (!link.startsWith("http://") && !link.startsWith("https://"))
                    link = absoluteURL(srcUrl, link);


                SimpleWrap<String> outUrl = new SimpleWrap<>(link);
                SimpleWrap<byte[]> outData = new SimpleWrap<>(new byte[0]);
                this.callback.loadFromWebByUrl(link, outUrl, outData);
                byte[] manifestData = outData.getValue();
                link = outUrl.getValue();

                if (manifestData == null || manifestData.length == 0) {

                    return 0;
                }

                JSONArray icons = new JSONObject(new String(manifestData)).getJSONArray("icons");
                int length = icons.length();
                for (int counter = 0; counter < length; counter++) {
                    JSONObject icon = icons.getJSONObject(counter);

                    String size = icon.getString("sizes");
                    String url = icon.getString("src");


                    if (isEmpty(url))
                        continue;

                    if (url.startsWith("//"))
                        url = parentScheme + ":" + url;

                    if (!link.startsWith("http://") && !link.startsWith("https://"))
                        url = absoluteURL(srcUrl, url);


                    //magic here
                    if (!isEmpty(size)) {
                        //magic
                        int mediumSize = (maxSize + minSize) / 2;
                        int toLow = mediumSize;
                        int toUp = mediumSize;
                        //soft search, equls "no forced"
                        for (int i = mediumSize; i <= maxPossibleIconSize; i++) {
                            if (toLow > minSize)
                                toLow--;
                            if (toUp < maxSize)
                                toUp++;
                            if (toLow == minSize && toUp == maxSize)
                                break;
                            if (toLow >= 0 && size.contains(toLow + "x" + toLow)) {
                                callback.logging("size: "+"sizes=\"" + toLow + "x" + toLow + "\"" +", link: "+url);
                                if ((int)handleIconUrl(url).first == RESULT_OK)
                                    resultCounter++;

                                continue;
                            }

                            if (toLow <= maxPossibleIconSize && size.contains(toUp + "x" + toUp)) {
                                callback.logging("size: "+"sizes=\"" + toUp + "x" + toUp + "\"" +", link: "+url);
                                if ((int)handleIconUrl(url).first == RESULT_OK)
                                    resultCounter++;
                            }

                        }

                        if (forced) {
                            for (int i = mediumSize; i <= maxPossibleIconSize; i++) {

                                if (toLow > 0)
                                    toLow--;
                                if (toUp < maxPossibleIconSize)
                                    toUp++;
                                if (toLow == 0 && toUp == maxPossibleIconSize + 1)
                                    break;
                                if (toLow >= 0 && size.contains(toLow + "x" + toLow)) {
                                    callback.logging("size: "+"sizes=\"" + toLow + "x" + toLow + "\"" +", link: "+url);
                                    if ((int)handleIconUrl(url).first  == RESULT_OK)
                                        resultCounter++;
                                    continue;
                                }

                                if (toLow <= maxPossibleIconSize && size.contains(toUp + "x" + toUp)) {
                                    callback.logging("size: "+"sizes=\"" + toUp + "x" + toUp + "\"" +", link: "+url);
                                    if ((int)handleIconUrl(url).first  == RESULT_OK)
                                        resultCounter++;
                                }


                            }
                        }


                    } else {
                        boolean forceLoad = forced;
                        if (!forceLoad)
                            forceLoad = (minSize <= 16 && 16 <= maxSize);
                        if (!forceLoad)
                            forceLoad = (minSize <= 24 && 24 <= maxSize);
                        if (!forceLoad)
                            forceLoad = (minSize <= 32 && 32 <= maxSize);
                        if (!forceLoad)
                            forceLoad = (minSize <= 48 && 48 <= maxSize);

                        if (forceLoad) {
                            callback.logging("size: Unknown, link: "+url);
                            if ((int)handleIconUrl(url).first  == RESULT_OK)
                                resultCounter++;
                        }
                    }
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultCounter;
    }


    //convert relative path ro absolute
    private String absoluteURL(String baseUrl, String relativeUrl) {
        SimpleWrap<String> outUrl = new SimpleWrap<>(baseUrl);
        this.callback.resolveURL(baseUrl, relativeUrl, outUrl);
        return outUrl.getValue();
    }

    private boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

}