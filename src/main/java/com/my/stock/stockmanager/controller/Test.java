package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.api.ebest.EbestApi;
import com.my.stock.stockmanager.dto.ebest.stock.request.t3320InBlock;
import com.my.stock.stockmanager.dto.ebest.stock.response.FNGSummary;
import com.my.stock.stockmanager.utils.EbestTokenProvider;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class Test {

	@GetMapping
	public void test() throws Exception {


	}
}
