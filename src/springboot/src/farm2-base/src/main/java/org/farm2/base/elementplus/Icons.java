package org.farm2.base.elementplus;

public enum Icons {

    HomeFilled("<svg  xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 1024 1024\"><path fill=\"currentColor\" d=\"M512 128 128 447.936V896h255.936V640H640v256h255.936V447.936z\"></path></svg>");


    private String svg;

    Icons(String svg) {
        this.svg = svg;
    }

    public String getSvg() {
        return svg;
    }

    public void setSvg(String svg) {
        this.svg = svg;
    }
}
