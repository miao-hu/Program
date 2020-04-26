$.ajax(
    {
        method: "get",        // 发起 ajax 请求时，使用什么 http 方法
        url: "rank.json",     // 请求哪个 url   (此 url 调用 RankServlet)
        dataType: "json",     // 返回的数据当成什么格式解析
        success: function (data) {  // 成功后，执行什么方法
            var names = [];     // 诗人
            var counts = [];    // 数量

            // data 中存放的是 json 格式内容
            for (var i in data) {
                names.push(data[i][0]);      //杜甫
                counts.push(data[i][1]);     //39
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
                        data: counts   // 作者对应的诗词数量
                    }
                ]
            };

            myChart.setOption(option);   //图表展示
        }
    }
);
