package com.github.buoooou.test.javabetter.top.copydown.strategy;

import com.github.buoooou.test.javabetter.top.furstenheim.CopyDown;
import lombok.Data;
import org.jsoup.nodes.Document;

@Data
public class Coverter {
    private CopyDown copyDown;
    private Document document;

    public Coverter(CopyDown copyDown, Document document) {
        this.copyDown = copyDown;
        this.document = document;
    }
}
