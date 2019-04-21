package com.hikvision.ga;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;

public class ArtemisPostTest {
	/**
	 * ������Լ���appKey��appSecret����static��̬���е���������.
	 * [1 host]
	 * 		�����ѡ�����ͬ����ƽ̨�Խ�,Ҳ����˵���ֳ�����,�����ܲ�����ʾ����,host�����޸�.Ĭ��Ϊip:port/artemis/api
	 * 		�����ѡ�����ͬ�ֳ������Խ�,hostҪ�޸�Ϊ�ֳ�������ip,����1.0 ʱ���˿�Ĭ��Ϊ9999.����2.0 ʱ���˿�Ĭ��Ϊ443.����:10.33.25.22:9999 ����10.33.25.22:443 [2 appKey��appSecret]
	 * [2 appKey��appSecret]
	 * 		�밴�ջ�õ���appKey��appSecret����.
	 * 
	 * ps. ���ͬ��������ƽ̨����ʾ�����Խӳɹ���,�л����ֳ�����,�밴���ֳ����,������������.
	 *
	 * TODO ����ǰ����ӿڴ������ʲô���Ǵ���json����doPostStringArtemis�������Ǳ��ύ����doPostFromArtemis����
	 * 
	 */
	static {
		ArtemisConfig.host = "10.33.47.50:443"; // artemis���ط�����ip�˿�
		ArtemisConfig.appKey = "28601151"; // ��Կappkey
		ArtemisConfig.appSecret = "wqaVdUU88PHjxuGn71yD";// ��ԿappSecret
	}
	/**
	 * ��������ƽ̨����վ·��
	 * TODO ·�������޸ģ�����/artemis
	 */
	private static final String ARTEMIS_PATH = "/artemis";

	/**
	 * ����POST�������ͽӿڣ������Ի�ȡ��֯�б�Ϊ��
	 * https://ip:port/artemis/api/resource/v1/org/orgList
	 *
	 * @return
	 */
	public static String callPostApiGetOrgList() {
		/**
		 * https://ip:port/artemis/api/resource/v1/org/orgList
		 * ����API�ĵ����Կ�����,����һ��POST�����Rest�ӿ�, ���Ҵ���Ĳ���ΪJSON�ַ���.
		 * ArtemisHttpUtil�������ṩ��doPostFormArtemis�������, һ������������ĵ���д�����е���˼. ��Ϊ�ӿ���https,
		 * ���Ե�һ������path�Ǹ�hashmap����,��putһ��key-value, querysΪ����Ĳ���. 
		 * body ΪJSON�ַ���.
		 * query������,���Դ���null,accept��contentType��ָ������Ĭ�ϴ�null.
		 */
		String  getCamsApi = ARTEMIS_PATH + "/api/resource/v1/org/orgList";
		Map<String, String> paramMap = new HashMap<String, String>();// post����Form������
		paramMap.put("pageNo", "1");
		paramMap.put("pageSize", "2");
		String body = JSON.toJSON(paramMap).toString();
		Map<String, String> path = new HashMap<String, String>(2) {
			{
				put("https://", getCamsApi);
			}
		};
		String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json");
		return result;
	}


	/**
	 * ����POST�������ͽӿڣ������Է�ҳ��ȡ�����б�Ϊ��
	 * https://ip:port/artemis/api/api/resource/v1/regions
	 *
	 * @return
	 */
	public static String callPostApiGetRegions(){
		/**
		 * https://ip:port/artemis/api/resource/v1/regions
		 * ����API�ĵ����Կ�����,����һ��POST�����Rest�ӿ�, ���Ҵ���Ĳ���ΪJSON�ַ���.
		 * ArtemisHttpUtil�������ṩ��doPostFormArtemis�������, һ������������ĵ���д�����е���˼. ��Ϊ�ӿ���https,
		 * ���Ե�һ������path�Ǹ�hashmap����,��putһ��key-value, querysΪ����Ĳ���.
		 * body ΪJSON�ַ���.
		 * query������,���Դ���null,accept��contentType��ָ������Ĭ�ϴ�null.
		 */
		String getCamsApi = ARTEMIS_PATH + "/api/resource/v1/regions";
		Map<String, String> paramMap = new HashMap<String, String>();// post����Form������
		paramMap.put("pageNo", "1");
		paramMap.put("pageSize", "2");
		String body = JSON.toJSON(paramMap).toString();
		Map<String, String> path = new HashMap<String, String>(2) {
			{
				put("https://", getCamsApi);
			}
		};
		String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json");
		return result;
	}

	public static void main(String[] args) {
		String result = callPostApiGetOrgList();
		System.out.println(result);

		String VechicleDataResult = callPostApiGetRegions();
		System.out.println(VechicleDataResult);
	}


}
