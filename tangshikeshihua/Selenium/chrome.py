from selenium import webdriver    # 导入浏览器驱动
import unittest                   # 导入 unittest 测试框架
import time

# 该类继承自 unittest.TestCase 类
class imageTest(unittest.TestCase):
    # 初始化工作（获得浏览器驱动，打开被测试系统,self 是该类的一个实例）
    def setUp(self):
        self.driver=webdriver.Chrome()
        self.driver.get("http://127.0.0.1:8080/tangshisanbaishou/index.html")
        self.driver.maximize_window()
        time.sleep(2)

    # 清理工作
    def tearDown(self):
        self.driver.quit()

    def test_first(self):
        self.driver.find_element_by_xpath("/html/body/div[1]/input[1]").click()
        time.sleep(5)

    @unittest.skip("skipping")
    def test_second(self):
        self.driver.find_element_by_xpath("/html/body/div[1]/input[2]").click()
        time.sleep(5)

if __name__=="__main__":
    unittest.main()
