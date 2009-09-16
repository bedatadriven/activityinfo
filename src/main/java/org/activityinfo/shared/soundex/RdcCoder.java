package org.activityinfo.shared.soundex;

public class RdcCoder implements SoundexCoder {
	
	  public RdcCoder()
      {
          //   romanNumeralRegex = new Regex("^x{0,i{,4}v?
          longVowels = new String[] { "ay", "aa", "ai", "au", "ee", "ei", "ey", "oo", "oi", "oa", "uu", "uy", "oy", "eu" };

      }
	  
	  private boolean isLongVowel(String s) {
		  for(String lv : longVowels) {
			  if(s.equals(lv)) {
				  return true;
			  }
		  }
		  return false;
	  }

      // private Regex romanNumeralRegex;

      private boolean isVowel(char c)
      {
          return (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == 'y');
      }
      
      private boolean isVowel(String s)
      {
          return s.length() == 1 && isVowel(s.charAt(0));
      }

      private boolean isRomanNumeral(String s)
      {
          return (s.equals("ii") || s.equals("iii") || s.equals("iv") || s.equals("v") || s.equals("vi") || s.equals("vii"));
      }

      public String encode(String value, boolean preserveSpaces)
      {
          value = value.toLowerCase() + " ";

          StringBuilder words = new StringBuilder(value.length());
          StringBuilder word = new StringBuilder(value.length());

          for (int i = 0; i != value.length(); ++i)
          {
              char c = value.charAt(i);
              if (c == '\'' || c == '`') // might be specific to afghanistan?
                  word.append('-');
              if (Character.isLetter(c))
              {
                  word.append(c);
              }
              else if (word.length() != 0)
              {
                  String ws = word.toString();
                  if (!isVowel(ws) && !isRomanNumeral(ws))
                  {
                      if (preserveSpaces && words.length() != 0)
                          words.append(' ');

                      words.append(codePhenomes(ws));
                  }
                  word.delete(0, word.length());
              }
          }
          return words.toString();
      }

      private String[] longVowels;

      private String codePhenomes(String word)
      {
          int len = word.length();
          int i = 0;
          String lastConsonant = "";
          StringBuilder codes = new StringBuilder(word.length());

          if(len==0)
        	  return "";
          
          while (i != len)
          {
              char c1;                 // the next single character
              String c2;  // the next two characters
              char n1;    // character after next
              char n2;    // character after after next
              char p1;    // character before last

              c1 = word.charAt(i);
              if (i + 1 < len)
              {
                  c2 = word.substring(i, i+1);
                  n1 = word.charAt(i+1);
              }
              else
              {
                  c2 = "";
                  n1 = 0;
              }
              if (i + 2 < len)
              {
                  n2 = word.charAt(i+2);
              }
              else
              {
                  n2 = 0;
              }
              if (i != 0)
                  p1 = word.charAt(i-1);
              else
                  p1 = 0;

              if (c1 == '-')
              {
                  i++;
              }
              else if (isVowel(c1) && (c1 != 'y' || i != 0))
              {
                  // normally we can't tell the difference between a
                  // consonal y ("yah!") and a vowel ("aiy", "choy") but
                  // if a word starts with a "y", it must be consonal.

                  codes.append('0');
                  i++;

                  if (isLongVowel(c2) ||
                     (c1 == 'y' && isVowel(n2)))
                  {
                      i++;
                  }
                  lastConsonant = "";
              }
              else
              {
                  String code;
                  if (c2.equals("dj"))
                  {
                      // Ignore d's that procede j's (djugo = jugo)
                      code = "j";
                      i += 2;
                  }
                  else if (c2.equals("gb"))
                  {
                      // Treat gb and b's the same (Gbadolite=Badolite)
                      code = "b";
                      i += 2;
                  }
                  else if (c1 == 'n' && !isVowel(n1))
                  {
                      // Drop n's that procede consonants (Ngote = Gote)
                      code = "";
                      i++;
                  }
                  else if (c1 == 'z')
                  {
                      // Treat s and z the same
                      code = "s";
                      i++;
                  }
                  else if (c2.equals("sh"))
                  {
                      // The sh often is eroded to s
                      code = "s";
                      i += 2;
                  }
                  else if (c1 == 'g' && isVowel(p1) && isVowel(n1))
                  {
                      // g's between two vowels are often dropped
                      // e.g. kisugu => kisou
                      code = "";
                      i++;
                  }
                  else if (c2.equals("gh") && isVowel(p1) && isVowel(n2))
                  {
                      code = "";
                      i += 2;
                  }
                  else if (c2.equals("zh"))
                  {
                      // this is a single consonant; we just need am arbitrary one-character
                      // code for it.
                      code = "5";
                      i += 2;
                  }
                  else if (c1 == 'w')
                  {
                      // only consider a 'w' a consonant when seperating two vowels
                      // a vowel, or if it comes at the beginning of a word.
                	  // otherwise it just adds flavor.
                      // Kawai = > k0w0
                      // Bwalu => b0l0
                      if (i==0 || (isVowel(p1) && isVowel(n1)))
                      {
                          code = "w";
                      }
                      else
                      {
                          code = "";
                      }
                      i++;
                  }
                  else if (c1 == 'h')
                  {
                      // ignore totally
                      code = "";
                      i++;
                  }
                  else
                  {
                      code = Character.toString(c1);
                      i++;
                  }

                  if (!code.equals(lastConsonant))
                      codes.append(code);

                  lastConsonant = code;

              }
          }

          return codes.toString();
      }
}
