package me.zpsw.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author: Zpsw
 * @Date: 2022/1/12
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Component
public class TestService {

    public void test() {
        log.info("Hello World");
    }
}
