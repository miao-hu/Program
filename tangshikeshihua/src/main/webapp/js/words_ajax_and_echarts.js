$.ajax({
    method: "get",            // 发起 ajax 请求时，使用什么 http 方法
    url: "words.json",         // 请求哪个 url   (此 url 调用 WordsServlet)
    dataType: "json",          // 返回的数据当成什么格式解析
    success: function (jsonData) {
        var myChart = echarts.init(document.getElementById('main'));     //初始化图表的画布

        var data = [];     //数组里面存放的内容：{“词”，“数量”}
        //请求返回的数据是 jsonData
        for (var i in jsonData) {
            var word = jsonData[i][0];   //词
            var count = jsonData[i][1];  //数量
            data.push({
                name: word,     //词的内容
                value: count,   //该词的数量
                textStyle: {
                    normal: {},
                    emphasis: {}
                }
            });
        }
        console.log(data);   //控制台打印

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
                data: data
            }]
        };

        myChart.setOption(option);   //图表展示
    }
});
