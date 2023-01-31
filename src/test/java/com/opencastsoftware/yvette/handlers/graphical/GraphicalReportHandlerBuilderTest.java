package com.opencastsoftware.yvette.handlers.graphical;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;
import com.opencastsoftware.yvette.LinkStyle;

import nl.jqno.equalsverifier.EqualsVerifier;

public class GraphicalReportHandlerBuilderTest {

    @Test
    void testSetsHyperlinkStyle() {
        GraphicalReportHandler textLinkHandler = GraphicalReportHandler.builder()
            .withTerminalLinks(false)
            .buildFor(new StringBuilder());

        assertThat(textLinkHandler.linkStyle(), is(equalTo(LinkStyle.TEXT)));

        GraphicalReportHandler hyperlinkHandler = GraphicalReportHandler.builder()
            .withTerminalLinks(true)
            .buildFor(new StringBuilder());

        assertThat(hyperlinkHandler.linkStyle(), is(equalTo(LinkStyle.HYPERLINK)));

        // StringBuilder is not detected as a TTY, so if we don't force hyperlinks, they will be disabled
        GraphicalReportHandler defaultHandler = GraphicalReportHandler.builder()
            .buildFor(new StringBuilder());

        assertThat(defaultHandler.linkStyle(), is(equalTo(LinkStyle.TEXT)));
    }

    @Test
    void testSetsTerminalWidth() {
        GraphicalReportHandler wideTermHandler = GraphicalReportHandler.builder()
            .withTerminalWidth(200)
            .buildFor(new StringBuilder());

        assertThat(wideTermHandler.terminalWidth(), is(equalTo(200)));

        GraphicalReportHandler narrowTermHandler = GraphicalReportHandler.builder()
            .withTerminalWidth(40)
            .buildFor(new StringBuilder());

        assertThat(narrowTermHandler.terminalWidth(), is(equalTo(40)));

        // StringBuilder is not detected as a TTY, so it receives a default value
        GraphicalReportHandler defaultHandler = GraphicalReportHandler.builder()
            .buildFor(new StringBuilder());

        assertThat(defaultHandler.terminalWidth(), is(equalTo(80)));
    }

    @Test
    void testSetsCauseChain() {
        GraphicalReportHandler causeChainHandler = GraphicalReportHandler.builder()
            .withCauseChain(true)
            .buildFor(new StringBuilder());

        assertThat(causeChainHandler.renderCauseChain(), is(true));

        GraphicalReportHandler noCauseChainHandler = GraphicalReportHandler.builder()
            .withCauseChain(false)
            .buildFor(new StringBuilder());

        assertThat(noCauseChainHandler.renderCauseChain(), is(false));

        GraphicalReportHandler defaultHandler = GraphicalReportHandler.builder()
            .buildFor(new StringBuilder());

        assertThat(defaultHandler.renderCauseChain(), is(true));
    }

    @Test
    void testSetsContextLines() {
        GraphicalReportHandler noContextHandler = GraphicalReportHandler.builder()
            .withContextLines(0)
            .buildFor(new StringBuilder());

        assertThat(noContextHandler.contextLines(), is(equalTo(0)));

        GraphicalReportHandler muchContextHandler = GraphicalReportHandler.builder()
            .withContextLines(3)
            .buildFor(new StringBuilder());

        assertThat(muchContextHandler.contextLines(), is(equalTo(3)));
    }

    @Test
    void testSetsFooter() {
        GraphicalReportHandler noFooterHandler = GraphicalReportHandler.builder()
            .withFooter(null)
            .buildFor(new StringBuilder());

        assertThat(noFooterHandler.footer(), is(nullValue()));

        GraphicalReportHandler someFooterHandler = GraphicalReportHandler.builder()
            .withFooter("footer")
            .buildFor(new StringBuilder());

        assertThat(someFooterHandler.footer(), is(equalTo("footer")));

        GraphicalReportHandler defaultHandler = GraphicalReportHandler.builder()
            .buildFor(new StringBuilder());

        assertThat(defaultHandler.footer(), is(nullValue()));
    }

    @Test
    void testSetsTheme() {
        GraphicalReportHandler themeHandler = GraphicalReportHandler.builder()
            .withGraphicalTheme(GraphicalTheme.emoji())
            .buildFor(new StringBuilder());

        assertThat(themeHandler.theme().characters(), is(equalTo(ThemeCharacters.emoji())));

        // Theme supersedes other style properties
        GraphicalReportHandler asciiHandler = GraphicalReportHandler.builder()
            .withGraphicalTheme(GraphicalTheme.ascii())
            .withUnicode(true)
            .buildFor(new StringBuilder());

        assertThat(asciiHandler.theme().characters(), is(equalTo(ThemeCharacters.ascii())));

        GraphicalReportHandler noneHandler = GraphicalReportHandler.builder()
            .withGraphicalTheme(GraphicalTheme.none())
            .withUnicode(true)
            .withColours(true)
            .withRgbColours(RgbColours.ALWAYS)
            .buildFor(new StringBuilder());

        assertThat(noneHandler.theme().styles(), is(equalTo(ThemeStyles.none())));
    }

    @Test
    void testSetsColours() {
        GraphicalReportHandler ansiHandler = GraphicalReportHandler.builder()
            .withColours(true)
            .withRgbColours(RgbColours.NEVER)
            .buildFor(new StringBuilder());

        assertThat(ansiHandler.theme().styles(), is(equalTo(ThemeStyles.ansi())));

        GraphicalReportHandler rgbHandler = GraphicalReportHandler.builder()
            .withColours(true)
            .withRgbColours(RgbColours.ALWAYS)
            .buildFor(new StringBuilder());

        assertThat(rgbHandler.theme().styles(), is(equalTo(ThemeStyles.rgb())));

        // StringBuilder is not detected as a TTY, so if we don't force colours, they will be disabled
        GraphicalReportHandler defaultHandler = GraphicalReportHandler.builder()
            .withRgbColours(RgbColours.ALWAYS)
            .buildFor(new StringBuilder());

        assertThat(defaultHandler.theme().styles(), is(equalTo(ThemeStyles.none())));
    }

    @Test
    void testSetsUnicode() {
        GraphicalReportHandler unicodeHandler = GraphicalReportHandler.builder()
            .withUnicode(true)
            .buildFor(new StringBuilder());

        assertThat(unicodeHandler.theme().characters(), is(equalTo(ThemeCharacters.unicode())));

        GraphicalReportHandler asciiHandler = GraphicalReportHandler.builder()
            .withUnicode(false)
            .buildFor(new StringBuilder());

        assertThat(asciiHandler.theme().characters(), is(equalTo(ThemeCharacters.ascii())));

        // StringBuilder is not detected as a TTY, so if we don't force unicode off, then it will be enabled
        GraphicalReportHandler defaultHandler = GraphicalReportHandler.builder()
            .buildFor(new StringBuilder());

        assertThat(defaultHandler.theme().characters(), is(equalTo(ThemeCharacters.unicode())));
    }

    @Test
    void testEquals() {
        EqualsVerifier.simple().forClass(GraphicalReportHandler.Builder.class).usingGetClass().verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(GraphicalReportHandler.Builder.class).verify();
    }
}
