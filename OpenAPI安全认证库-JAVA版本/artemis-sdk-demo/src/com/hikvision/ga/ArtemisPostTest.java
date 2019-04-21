package com.hikvision.ga;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;

public class ArtemisPostTest {
	/**
	 * 请根据自己的appKey和appSecret更换static静态块中的三个参数.
	 * [1 host]
	 * 		如果你选择的是同开放平台对接,也就是说非现场环境,海康总部的演示环境,host不用修改.默认为ip:port/artemis/api
	 * 		如果你选择的是同现场环境对接,host要修改为现场环境的ip,网关1.0 时，端口默认为9999.网关2.0 时，端口默认为443.例如:10.33.25.22:9999 或者10.33.25.22:443 [2 appKey和appSecret]
	 * [2 appKey和appSecret]
	 * 		请按照或得到的appKey和appSecret更改.
	 * 
	 * ps. 如果同海康开放平台的演示环境对接成功后,切换到现场环境,请按照现场情况,更换三个参数.
	 *
	 * TODO 调用前看清接口传入的是什么，是传入json就用doPostStringArtemis方法，是表单提交就用doPostFromArtemis方法
	 * 
	 */
	static {
		ArtemisConfig.host = "10.33.47.50:443"; // artemis网关服务器ip端口
		ArtemisConfig.appKey = "28601151"; // 秘钥appkey
		ArtemisConfig.appSecret = "wqaVdUU88PHjxuGn71yD";// 秘钥appSecret
	}
	/**
	 * 能力开放平台的网站路径
	 * TODO 路径不用修改，就是/artemis
	 */
	private static final String ARTEMIS_PATH = "/artemis";

	/**
	 * 调用POST请求类型接口，这里以获取组织列表为例
	 * https://ip:port/artemis/api/resource/v1/org/orgList
	 *
	 * @return
	 */
	public static String callPostApiGetOrgList() {
		/**
		 * https://ip:port/artemis/api/resource/v1/org/orgList
		 * 根据API文档可以看出来,这是一个POST请求的Rest接口, 而且传入的参数为JSON字符串.
		 * ArtemisHttpUtil工具类提供了doPostFormArtemis这个函数, 一共五个参数在文档里写明其中的意思. 因为接口是https,
		 * 所以第一个参数path是个hashmap类型,请put一个key-value, querys为传入的参数. 
		 * body 为JSON字符串.
		 * query不存在,所以传入null,accept和contentType不指定按照默认传null.
		 */
		String  getCamsApi = ARTEMIS_PATH + "/api/resource/v1/org/orgList";
		Map<String, String> paramMap = new HashMap<String, String>();// post请求Form表单参数
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
	 * 调用POST请求类型接口，这里以分页获取区域列表为例
	 * https://ip:port/artemis/api/api/resource/v1/regions
	 *
	 * @return
	 */
	public static String callPostApiGetRegions(){
		/**
		 * https://ip:port/artemis/api/resource/v1/regions
		 * 根据API文档可以看出来,这是一个POST请求的Rest接口, 而且传入的参数为JSON字符串.
		 * ArtemisHttpUtil工具类提供了doPostFormArtemis这个函数, 一共五个参数在文档里写明其中的意思. 因为接口是https,
		 * 所以第一个参数path是个hashmap类型,请put一个key-value, querys为传入的参数.
		 * body 为JSON字符串.
		 * query不存在,所以传入null,accept和contentType不指定按照默认传null.
		 */
		String getCamsApi = ARTEMIS_PATH + "/api/resource/v1/regions";
		Map<String, String> paramMap = new HashMap<String, String>();// post请求Form表单参数
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
