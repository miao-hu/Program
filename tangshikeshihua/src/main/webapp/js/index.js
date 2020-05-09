var rank= function() {
    $.ajax(
        {
            method: "get",        // 发起 ajax 请求时，使用什么 http 方法
            url: "rank.json",     // 请求哪个 url   (此 url 调用 RankServlet)
            dataType: "json",     // 返回的数据当成什么格式解析
            success: function (data) {    // 成功后，执行什么方法
                var names = [];     // 诗人
                var counts = [];    // 对应的创作的数量

                // data 中存放的是 json 格式的内容 （响应内容）
                for (var i in data) {
                    names.push(data[i][0]);      //杜甫，李白
                    counts.push(data[i][1]);     //39，21
                }

                console.log(names);      //控制台输出所有诗人的姓名
                console.log(counts);     //控制台输出所有诗人的诗词数量

                var myChart = echarts.init(document.getElementById('main'));

                var option = {
                    // 图标的标题
                    title: {
                    },
                    tooltip: {},
                    legend: {
                        data:['数量']
                    },
                    // 横坐标
                    xAxis: {
                        data: names   //所有作者的姓名
                    },
                    yAxis: {},
                    series: [
                        {
                            name: '数量',
                            type: 'bar',    // bar 代表柱状图
                            //渐变色颜色
                            itemStyle: {
                                color: new echarts.graphic.LinearGradient(
                                    0, 0, 0, 1,
                                    [
                                        {offset: 0, color: '#83bff6'},
                                        {offset: 0.5, color: '#188df0'},
                                        {offset: 1, color: '#188df0'}
                                    ]
                                )
                            },
                            emphasis: {
                                itemStyle: {
                                    color: new echarts.graphic.LinearGradient(
                                        0, 0, 0, 1,
                                        [
                                            {offset: 0, color: '#2378f7'},
                                            {offset: 0.7, color: '#2378f7'},
                                            {offset: 1, color: '#83bff6'}
                                        ]
                                    )
                                }
                            },
                            data: counts   // 作者对应的创作数量
                        }
                    ]
                };

                //使用刚指定过的配置项和数据填充图表
                myChart.setOption(option,true);
            }
        }
    );
};

var words = function () {
    $.ajax({
        method: "get",             // 发起 ajax 请求时，使用什么 http 方法
        url: "words.json",         // 请求哪个 url   (此 url 调用 WordsServlet)
        dataType: "json",          // 返回的数据当成什么格式解析
        success: function (jsonData) {
            var data = [];     //数组里面存放的内容：{“词”，“数量”}

            //请求返回的数据是 jsonData （响应内容）
            for (var i in jsonData) {
                var word = jsonData[i][0];   //词
                var count = jsonData[i][1];  //对应的数量

                data.push({
                    name: word,     //词的内容
                    value: count,   //该词的数量
                    textStyle: {   //显示样式
                        normal: {},
                        emphasis: {}
                    }
                });
            }
            console.log(data);   //控制台打印

            var myChart = echarts.init(document.getElementById('main'));     //初始化图表的画布

            var option = {
                series: [{
                    type: 'wordCloud',
                    shape: 'pentagon',
                    left: 'center',
                    top: 'center',
                    width: '80%',
                    height: '80%',
                    right: null,
                    bottom: null,
                    sizeRange: [12, 60],
                    rotationRange: [-90, 90],
                    rotationStep: 45,
                    gridSize: 8,
                    drawOutOfBound: false,
                    textStyle: {
                        normal: {
                            fontFamily: 'sans-serif',
                            fontWeight: 'bold',
                            color: function () {
                                return 'rgb(' + [
                                    Math.round(Math.random() * 160),          // 设置随机颜色
                                    Math.round(Math.random() * 160),
                                    Math.round(Math.random() * 160)
                                ].join(',') + ')';
                            }
                        },
                        emphasis: {
                            shadowBlur: 10,
                            shadowColor: '#333'
                        }
                    },
                    data: data   //data 就是所有的词以及它们对应的数量
                }]
            };

            //使用刚指定过的配置项和数据填充图表
            myChart.setOption(option,true);
        }
    });
}
