package org.vazhmax.trellish.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Service
public final class LexorankService {
    private static final byte ALPHABET_SIZE = 26;

    public String findMedian(String first, String second) {
        while (first.length() != second.length()) {
            if (first.length() > second.length())
                second += "a";
            else
                first += "a";
        }

        var firstPositionCodes = first.getBytes(StandardCharsets.US_ASCII);
        var secondPositionCodes = second.getBytes(StandardCharsets.US_ASCII);

        var difference = 0;


        for (int index = firstPositionCodes.length - 1; index >= 0; index--) {
            /// Codes of the elements of positions
            var firstCode = firstPositionCodes[index];
            var secondCode = secondPositionCodes[index];

            /// i.e. ' a < b '
            if (secondCode < firstCode) {
                secondCode += ALPHABET_SIZE;
                secondPositionCodes[index - 1] -= 1;
            }

            /// formula: x = a * size^0 + b * size^1 + c * size^2
            final var powRes = (int) Math.pow(ALPHABET_SIZE, first.length() - index - 1);
            difference += (secondCode - firstCode) * powRes;
        }

        StringBuilder newElement = new StringBuilder();
        if (difference <= 1) {
            newElement = new StringBuilder(first + (char)((int)'a' + ALPHABET_SIZE / 2));
        } else {
            difference /= 2;

            var offset = 0;
            for (int index = 0; index < first.length(); index++) {
                /// formula: x = difference / (size^place - 1) % size;
                /// i.e. difference = 110, size = 10, we want place 2 (middle),
                /// then x = 100 / 10^(2 - 1) % 10 = 100 / 10 % 10 = 11 % 10 = 1
                var diffInSymbols = difference / (int) Math.pow(ALPHABET_SIZE, index) % (ALPHABET_SIZE);

                var newElementCode = first.getBytes()[second.length() - index - 1] + diffInSymbols + offset;
                offset = 0;

                // if newElement is greater than 'z'
                if (newElementCode > (int)'z') {
                    offset++;
                    newElementCode -= ALPHABET_SIZE;
                }

                newElement.append((char)newElementCode);
            }
            newElement.reverse();
        }

        return newElement.toString();
    }
}
