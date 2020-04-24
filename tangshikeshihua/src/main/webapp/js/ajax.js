$.ajax(
    {
        method: "get",       // 发起 ajax 请求时，使用什么 http 方法
        url: "staticRank.json",    // 请求哪个 url
        dataType: "json",    // 返回的数据当成什么格式解析
        success: function (data) {    // 成功后，执行什么方法
            console.log("Begin")
            console.log(data);     //控制台打印数据
            console.log("End")
        }
    }
);
