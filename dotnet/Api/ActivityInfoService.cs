using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Linq;
using System.Text;
using System.Net;
using System.IO;
using System.Web;
using JsonExSerializer;
using JsonExSerializer.MetaData;

namespace ActivityInfo.Api
{
    public class ActivityInfoService
    {
        private string baseUrl;
        private string authToken;

        public ActivityInfoService(string baseUrl, string email, string password)
        {
            this.baseUrl = baseUrl;
            login(email, password);
        }

        public ActivityInfoService(string email, string password)
        {
            this.baseUrl = "http://www.activityinfo.org/";
            login(email, password);
        }

        public SyncRegions GetSyncRegions()
        {
            Serializer serializer = new Serializer(typeof(SyncRegions));
            serializer.Config.TypeHandlerFactory.SetPropertyNamingStrategy(new CamelCaseNamingStrategy());

            return (SyncRegions)doGet("SyncRegions", new NameValueCollection(), serializer);
        }

        public SyncRegionUpdates GetSyncRegionUpdates(string regionId, string localVersion) {

            NameValueCollection parameters = new NameValueCollection();
            parameters.Add("regionId", regionId);
            if (localVersion != null)
            {
                parameters.Add("localVersion", localVersion);
            }

            Serializer serializer = new Serializer(typeof(SyncRegionUpdates));
            serializer.Config.TypeHandlerFactory.SetPropertyNamingStrategy(new CamelCaseNamingStrategy());

            return (SyncRegionUpdates)doGet("SyncRegionUpdates", parameters, serializer);
        }

        private void login(String email, String password)
        {
            Uri uri = new Uri(baseUrl + "Login/service");
            String postData = "email=" + email + "&password=" + password;
            HttpWebRequest request = (HttpWebRequest)WebRequest.Create(uri);
            request.Method = "POST";
            request.ContentType = "application/x-www-form-urlencoded";
            request.ContentLength = postData.Length;
            request.CookieContainer = new CookieContainer();

            using (Stream writeStream = request.GetRequestStream())
            {
                UTF8Encoding encoding = new UTF8Encoding();
                byte[] bytes = encoding.GetBytes(postData);
                writeStream.Write(bytes, 0, bytes.Length);
            }

            String result;
            using (HttpWebResponse response = (HttpWebResponse)request.GetResponse())
            {
                using (Stream responseStream = response.GetResponseStream())
                {
                    using (StreamReader readStream = new StreamReader(responseStream, Encoding.UTF8))
                    {
                        result = readStream.ReadToEnd();
                    }
                }
                if (result.Contains("OK"))
                {
                    authToken = response.Cookies["authToken"].Value;
                }
                else
                {
                    throw new AuthenticationException();
                }
            }
        }

        private object doGet(String commandName, NameValueCollection parameters, Serializer serializer)
        {
            Uri uri = new Uri(baseUrl + "api/" + commandName + ToQueryString(parameters));
           
            HttpWebRequest request = (HttpWebRequest)WebRequest.Create(uri);
            request.Method = "GET";
            request.Headers.Add("X-ActivityInfo-AuthToken", authToken);
           

            using (HttpWebResponse response = (HttpWebResponse)request.GetResponse())
            {
                using (Stream responseStream = response.GetResponseStream())
                {
                    using (StreamReader readStream = new StreamReader(responseStream, Encoding.UTF8))
                    {
                        string json = readStream.ReadToEnd();
                        return serializer.Deserialize(json);
                    }
                }
            }
        }

        private string ToQueryString(NameValueCollection nvc)
        {
            if (nvc.Count == 0)
            {
                return "";
            }
            else
            {
                return "?" + string.Join("&", Array.ConvertAll(nvc.AllKeys,
                    key => string.Format("{0}={1}", HttpUtility.UrlEncode(key),
                            HttpUtility.UrlEncode(nvc[key]))));
            }
        }

    }
}


