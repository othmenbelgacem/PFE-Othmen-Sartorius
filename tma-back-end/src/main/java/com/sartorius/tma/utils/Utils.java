package com.sartorius.tma.utils;

import com.sartorius.tma.dtos.VerifiedFieldDto;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * @author Rokaya
 * @Date 16/07/2022
 */
public class Utils {
  public static String convertAmountToStringWithSeperator(double amount1) {
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
    symbols.setGroupingSeparator('\'');
    DecimalFormat df=new DecimalFormat("# ###.###", symbols);
    NumberFormat numberFormat = NumberFormat.getInstance(java.util.Locale.FRENCH);
    String initialAmount=numberFormat.format(amount1);

    if (initialAmount.contains(".") || initialAmount.contains(",")) {
      String [] tab = null;
      if(initialAmount.contains(".") ) {
        tab = initialAmount.split("\\.");
      } else {
        tab = initialAmount.split(",");
      }

      if(tab[1].length() == 1) {
        tab[1] = "." + tab[1] + "00";
        initialAmount = tab[0] + tab[1];
      } else if(tab[1].length() == 2) {
        tab[1] = "." + tab[1] + "0";
        initialAmount = tab[0] + tab[1];
      } else if(tab[1].length() == 3) {
        tab[1] = "." + tab[1];
        initialAmount = tab[0] + tab[1];
      }

      return initialAmount;
    } else {
      initialAmount = initialAmount + ".000";


      return initialAmount;
    }
  }

  public static List<VerifiedFieldDto> checkFieldsIsNull(Object instance, List<String> fieldNames) {
    List<VerifiedFieldDto> verifiedFieldDtos=new ArrayList<>();
    fieldNames.stream().forEach(field -> {
      try {
        java.lang.reflect.Field fieldName = getField(instance.getClass(),field);
        fieldName.setAccessible(true);
        Object value = fieldName.get(instance);
        if( value!=null && !value.toString().isEmpty()){
          verifiedFieldDtos.add(new VerifiedFieldDto(field,true));
        }
        else {
          verifiedFieldDtos.add(new VerifiedFieldDto(field, false));
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    return verifiedFieldDtos;
  }


  public static java.lang.reflect.Field getField(final Class clazz, String field_name) throws NoSuchFieldException {
    if(clazz == null || field_name == null)
      return null;
    java.lang.reflect.Field field=null;
    for(Class curr=clazz; curr != null; curr=curr.getSuperclass()) {
      try {
        return curr.getDeclaredField(field_name);
      }
      catch(NoSuchFieldException ignored) {
      }
    }
    if(field == null )
      throw new NoSuchFieldException(String.format("%s not found in %s or superclasses", field_name, clazz.getName()));
    return field;
  }

  public static String getSaltString() {
    String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    StringBuilder salt = new StringBuilder();
    Random rnd = new Random();
    while (salt.length() < 4) { // length of the random string.
      int index = (int) (rnd.nextFloat() * SALTCHARS.length());
      salt.append(SALTCHARS.charAt(index));
    }
    String saltStr = salt.toString();
    return saltStr;

  }

  public static String formatNumber(Long number){
    return String.format("%04d",number);
  }

}
