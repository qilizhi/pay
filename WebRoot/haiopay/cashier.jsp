<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <script src="js/jquery.pack.js"></script>
    <script>
        function formatDate(date, format) {
            var paddNum = function (num) {
                num += "";
                return num.replace(/^(\d)$/, "0$1");
            }
            var cfg = {
                yyyy: date.getFullYear(), yy: date.getFullYear().toString().substring(2), M: date.getMonth() + 1, MM: paddNum(date.getMonth() + 1), d: date.getDate(), dd: paddNum(date.getDate()), hh: date.getHours(), mm: date.getMinutes(), ss: date.getSeconds()
            }
            format || (format = "yyyy-MM-dd hh:mm:ss");
            return format.replace(/([a-z])(\1)*/ig, function (m) { return cfg[m]; });
        }
        $(document).ready(function () {
            $("#orderid").text('${param.orderid}');
            $("#dt").text(formatDate(new Date(), "yyyy-MM-dd hh:mm:ss"));
           var amt=  "${param.amt}";
           var orderid ="${param.orderid}";
           var att = "${param.banktype}";
           location.href = "haioPay.action?amt=" + amt + "&banktype=" + att + "&orderid="+orderid ;
        }
        );
    </script>
