package lab;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.util.List;

public class 分词Demo {
    public static void main(String[] args) {
        String sentence="中华人名共和国成立了！中国人民站起来了";
        //一个Term就是一个单词
        List<Term> termList=NlpAnalysis.parse(sentence).getTerms();
        for(Term term:termList){
            // 词性：词
            System.out.println(term.getNatureStr()+":"+term.getRealName());
        }

        /*
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
