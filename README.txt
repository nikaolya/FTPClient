??? ??????? ??????? ?????????? ????????? ??????? main ? ?????? Server, ? ????? ? Client.
? ????, ??? ? ??????? ???? ??????? ????????? jar ????,
?? ??? ??? ? ?? ??????? ??? ????????? ???????, ????? Server ? Client ??????????? ????????????. =(

???????, ??????????, ????????? ??? ?? ????? ?????????? (?? ??????? ? IntellijIdea).

?????: admin
??????: admin

?????????? ????????? ??????? ?????? ? "src/main/resources/config.properties".

???? ?????????:
    "0" - ????????? ?????? ???? ?????????. ???????? ????????????? ?? ?????.
	"1" - ????????? ?????? ????????? ? ?????????? ??????. ???????? ? ?????????? ?????? ????????????? ?? id.
	"2" - ????????? ?????????? ? ???????? ?? id. ???? ?????? id ?? ??????????, ???????????? null.
	"3" - ?????????? ?????? ???????? ?? ?????. Id ??????????????? ?????????????: ?????????? ???????? ?? ????????????? ? ???????.
	"4" - ???????? ???????? ?? id. ???? ?????? id ?? ??????????, ???????????? null.

??? ????????? ????????? ? ???????, ?????????? ????? "jsonString.txt" ??????????? ? ???? "temp.txt".
?????? ? ??????? jSON ???????? ? ?????? java - ????????? ?????? Students.
????????? ????????????? ????????? ????????? ???? ?????????, ???????? ?????? ??????? ? ??? ??????? ???????
(?????? parseJSONtoJavaObject(String fileName) ? parseStudentsObjectToJson(Students students)).
????? ?????????????? json ?????? ? ?????? ?????? Students, ??? ???????? ???????????? ??? ?????? ????????,
?? ??? ???, ???? ?????? ?? ?????????? ??????? ?? ??????. ? ???? ?????? ???? "temp.txt" ??????????? (????????????????)
? ??? ?????????? ???????????? ?? ??????, ??? ?????? ??????????? ? ???????? ???? "jsonString.txt".
??? ?????????? ?????? ? ?????, ?????????? ?????? ????????? ?????????????? ??????????? ?? id.
???? ????? ???????? "6" ????? ????????? ?????-?????? ????????, ???? "jsonString.txt" ????? ?????? ??????????? ? ???????.

    "5" ????????? ?????????? "temp.txt", ???????? ??? ? ???????, ?? ?? ???????? ?????? ?? ??????.
    "6" ????????? ?????????? "temp.txt", ???????? ??? ? ???????, ? ???????? ?????? ?? ??????.
    "7" ????????? ?????? ?????????, ???? "temp.txt" ?????????.
        ??????????????? ?????????? ?????? ?? ??????? (jsonString.txt) ?? ??????????.


? ???????? Unit ????? ?? ???????, ??????? ?? ????????? ?? ?????????????? ? ???????? ? ?????????? ??????. ??? ??? ??????? ? ?? ????. =(
????? ?? ???????, ?????????? ? ?????????? ???????, ? ?????????????? ? ????????? "0-4" ????????? ? ????? "src/test/java".
??? ??????? ? Test Suite ? ????? ???? ???????? ??? ????? ????? ?????? "testng.xml" ?????, ??? ????? ????? "mvn test" ?? ????? ???????, ??? ??? "testng.xml" ?????? ? <suiteXmlFiles> ? "pom.xml" ???????.




