package lol.tgformat.irc;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author TG_format
 * @since 2024/5/25 13:54
 */
@NoArgsConstructor
@Data
@Getter
public class ReceiveData {
    private String data;
    private String type;
}