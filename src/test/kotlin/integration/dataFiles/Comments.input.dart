abc();
/*
    logDebug('');
        logDebug(results[0]['yoast_head_json'].toString());
    logDebug(JsonTools.encode(results[0]['yoast_head_json']));
logDebug(results[0]['yoast_head_json']['description'].toString());
logDebug('');
*/

1

    results.addAll(JsonTools.decode<List<dynamic>>(response.body));
    /*
        logDebug('');
            logDebug(results[0]['yoast_head_json'].toString());
        logDebug(JsonTools.encode(results[0]['yoast_head_json']));
    logDebug(results[0]['yoast_head_json']['description'].toString());
logDebug('');
    */

2

{
results.addAll(JsonTools.decode<List<dynamic>>(response.body));
/*
    logDebug('');
        logDebug(results[0]['yoast_head_json'].toString());
    logDebug(JsonTools.encode(results[0]['yoast_head_json']));
logDebug(results[0]['yoast_head_json']['description'].toString());
logDebug('');
*/
}

3

{
    results.addAll(JsonTools.decode<List<dynamic>>(response.body));
    /*
        logDebug('');
            logDebug(results[0]['yoast_head_json'].toString());
        logDebug(JsonTools.encode(results[0]['yoast_head_json']));
    logDebug(results[0]['yoast_head_json']['description'].toString());
logDebug('');
    */
}

4

{
    {
        results.addAll(JsonTools.decode<List<dynamic>>(response.body));
        /*
            logDebug('');
                logDebug(results[0]['yoast_head_json'].toString());
            logDebug(JsonTools.encode(results[0]['yoast_head_json']));
        logDebug(results[0]['yoast_head_json']['description'].toString());
    logDebug('');
logDebug('X');
        */
    }
}

abc(); /*
1
    2
        3
*/

{
    abc(); /*
    1
        2
            3
    */
}
