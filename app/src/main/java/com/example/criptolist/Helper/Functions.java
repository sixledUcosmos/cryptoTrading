package com.example.criptolist.Helper;


import java.text.DecimalFormat;

public class Functions {

    DecimalFormat formato;
    public double  ConvertDoubleString(String price){

      return  Double.parseDouble(price.replaceAll(",", "."));
    }

    public String  FormatPrice(double number){

      if(number>0.1){
        formato = new DecimalFormat("0.00");
      }else{
        if(number>0.00001)
        {formato = new DecimalFormat("0.000000");}
        else {
          formato = new DecimalFormat("0.00000000");
        }
      }
      return formato.format(number);
    }
  public double FormatPriceAndConvertDouble(double number){
   String numberformat= FormatPrice(number);

    return  ConvertDoubleString(numberformat);
  }
}
