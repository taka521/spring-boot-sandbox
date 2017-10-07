package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * {@link org.springframework.web.servlet.DispatcherServlet} 用のコンフィグレーションクラス。
 * WebMvcConfigurerAdapter を継承することで、デフォルトで適用されるBean定義を簡単にカスタマイズできる。
 */
@Configuration  // コンフィグレーションクラスであることを示す
@EnableWebMvc   // SpringMVCを利用するにあたり、必要となるコンポーネントの定義を自動で行ってくれる
@ComponentScan("example.app") // 指定されたパッケージ配下のステレオタイプアノテーションを読み込んで、DIコンテナに登録する
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    // ビュー名を解決するための、ViewResolveを登録する。
    //   JSPの場合、デフォルトでは WEB-INF 配下の *.jsp ファイルがViewとした扱われる。
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/views/", ".jsp");
    }
}
