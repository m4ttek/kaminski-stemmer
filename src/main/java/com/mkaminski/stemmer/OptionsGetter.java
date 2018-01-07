package com.mkaminski.stemmer;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Gets and converts options from the application user.
 *
 * @author Mateusz Kamiński
 */
public class OptionsGetter {

    enum OptionCommand {
        CONVERT_COMMAND(Option.builder().longOpt("convert").desc("converts a dictionary to the internal representation").build()) {
            @Override
            public RunOptionsBuilder setValue(RunOptionsBuilder runOptionsBuilder, CommandLine commandLine) {
                if (commandLine.hasOption("convert")) {
                    runOptionsBuilder.setMainCommand("convert");
                }
                return runOptionsBuilder;
            }
        },
        STEM_COMMAND(Option.builder().longOpt("stem").desc("stems document").build()) {
            @Override
            public RunOptionsBuilder setValue(RunOptionsBuilder runOptionsBuilder, CommandLine commandLine) {
                if (commandLine.hasOption("stem")) {
                    runOptionsBuilder.setMainCommand("stem");
                }
                return runOptionsBuilder;
            }
        },
        SOURCE_PATH(Option.builder().longOpt("source").desc("source file").hasArg(true).required().build()) {
            @Override
            public RunOptionsBuilder setValue(RunOptionsBuilder runOptionsBuilder, CommandLine commandLine) {
                return runOptionsBuilder.setSourcePath(Paths.get(commandLine.getOptionValue("source")));
            }
        },
        DEST_PATH(Option.builder().longOpt("dest").desc("destination file").hasArg(true).required().build()) {
            @Override
            public RunOptionsBuilder setValue(RunOptionsBuilder runOptionsBuilder, CommandLine commandLine) {
                return runOptionsBuilder.setResultPath(Paths.get(commandLine.getOptionValue("dest")));
            }
        },
        DICT_PATH(Option.builder().longOpt("dict").desc("dictionary file to use").hasArg(true).build()) {
            @Override
            public RunOptionsBuilder setValue(RunOptionsBuilder runOptionsBuilder, CommandLine commandLine) {
                if (commandLine.getOptionValue("dict") == null) {
                    return runOptionsBuilder;
                }
                return runOptionsBuilder.setDictPath(Paths.get(commandLine.getOptionValue("dict")));
            }
        };

        private final Option commandOption;

        OptionCommand(Option commandOption) {
            this.commandOption = commandOption;
        }

        public abstract RunOptionsBuilder setValue(RunOptionsBuilder runOptionsBuilder, CommandLine commandLine);

        public static List<OptionCommand> getOptions() {
            return Arrays.asList(values());
        }

        public static RunOptions mapToRunOptions(CommandLine commandLine) {
            RunOptionsBuilder runOptionsBuilder = getOptions()
                    .stream()
                    .reduce(new RunOptionsBuilder(),
                            (builder, optionCommand) -> optionCommand.setValue(builder, commandLine),
                            (u, u2) -> u);
            if (runOptionsBuilder.getMainCommand() == null) {
                runOptionsBuilder.setMainCommand("stem");
            }
            return runOptionsBuilder.createRunOptions();
        }
    }

    public RunOptions parseCommands(String[] args) {
        Options options = OptionCommand.getOptions()
                .stream()
                .reduce(new Options(),
                        (optionsObject, option) -> optionsObject.addOption(option.commandOption),
                        (options1, options2) -> options1);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException pe) {
            System.err.println(pe.getMessage());
            System.exit(1);
            return new FailedRunOptions();
        }
        return OptionCommand.mapToRunOptions(cmd);
    }
}
