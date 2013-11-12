package net.simpleframework.mvc.component.portal.module;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class YahooWeatherUtils {
	static final String[] RFC822_MASKS = { "EEE, dd MMM yyyy HH:mm:ss z",
			"EEE, dd MMM yyyy hh:mm a z", "yyyy-MM-dd HH:mm:ss" };

	private static SimpleDateFormat format0 = new SimpleDateFormat("HH:mm");

	private static SimpleDateFormat format1 = new SimpleDateFormat("EEE");

	static String formatHour(final Date date) {
		return format0.format(date);
	}

	static String formatWeek(final Date date) {
		return format1.format(date);
	}

	static String[] yahooTexts = { "龙卷风, 大雷雨"
	/* 0: tornado */, "热带风暴"
	/* 1: tropical storm */, "飓风"
	/* 2: hurricane */, "剧烈雷暴"
	/* 3: severe thunderstorms */, "雷暴"
	/* 4: thunderstorms */, "雨夹雪"
	/* 5: mixed rain and snow */, "雨夹雹"
	/* 6: mixed rain and sleet */, "雪夹雹"
	/* 7: mixed snow and sleet */, "毛毛冷雨"
	/* 8: freezing drizzle */, "毛毛雨"
	/* 9: drizzle */, "冷雨"
	/* 10: freezing rain */, "阵雨"
	/* 11: showers */, "阵雨"
	/* 12: showers */, "小雪"
	/* 13: snow flurries */, "小阵雪"
	/* 14: light snow showers */, "高吹雪"
	/* 15: blowing snow */, "雪"
	/* 16: snow */, "冰雹"
	/* 17: hail */, "冰雨，雨夹雪"
	/* 18: sleet */, "扬尘"
	/* 19: dust */, "雾"
	/* 20: foggy */, "薄雾"
	/* 21: haze */, "烟雾"
	/* 22: smoky */, "大风"
	/* 23: blustery */, "强风"
	/* 24: windy */, "寒冷"
	/* 25: cold */, "多云"
	/* 26: cloudy */, "夜间多云间晴"
	/* 27: mostly cloudy (night) */, "白天多云间晴"
	/* 28: mostly cloudy (day) */, "夜间晴间多云"
	/* 29: partly cloudy (night) */, "白天晴间多云"
	/* 30: partly cloudy (day) */, "夜间晴"
	/* 31: clear (night) */, "丽日"
	/* 32: sunny */, "夜间转晴"
	/* 33: fair (night) */, "白天转晴"
	/* 34: fair (day) */, "雨夹雹"
	/* 35: mixed rain and hail */, "炎热"
	/* 36: hot */, "局部风暴"
	/* 37: isolated thunderstorms */, "小风暴"
	/* 38: scattered thunderstorms */, "小风暴"
	/* 39: scattered thunderstorms */, "小阵雨"
	/* 40: scattered showers */, "大雪"
	/* 41: heavy snow */, "小雪"
	/* 42: scattered snow showers */, "大雪"
	/* 43: heavy snow */, "晴间多云"
	/* 44: partly cloudy */, "雷阵雨"
	/* 45: thundershowers */, "雪夹雨"
	/* 46: snow showers */, "局部雷阵雨"
	/* 47: isolated thundershowers */, "不可用" };
}
