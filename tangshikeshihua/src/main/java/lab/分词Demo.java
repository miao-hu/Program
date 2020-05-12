package lab;
/*
    预言：此第三方库的简单使用
 */
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.util.List;

public class 分词Demo {
    public static void main(String[] args) {
        String sentence="中华人名共和国成立了！中国人民站起来了";
        //一个 Term 就是一个词
        List<Term> termList=NlpAnalysis.parse(sentence).getTerms();

        for(Term term:termList){
            System.out.println(term.getNatureStr()+":"+term.getRealName());
        }

        /*打印内容为    词性：词
                ns:中华人名共和国
                v:成立
                u:了
                w:！
                ns:中国
                n:人民
                v:站
                v:起来
                u:了
         */
    }
}
