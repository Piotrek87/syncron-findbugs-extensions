package com.syncron.bpp.findbugsextensions;

public class PrintStackTraceBug {

	public static void main(String[] args) {
		Exception e = new RuntimeException("test");
		e.printStackTrace();
	}
}
