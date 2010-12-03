package org.sigmah.shared.command.result;

import java.util.List;

import org.sigmah.shared.dto.history.HistoryTokenListDTO;

public class HistoryResult implements CommandResult {

    private static final long serialVersionUID = -238246727737468100L;

    private List<HistoryTokenListDTO> tokens;

    public HistoryResult() {
        // Serialization.
    }

    public HistoryResult(List<HistoryTokenListDTO> tokens) {
        this.tokens = tokens;
    }

    public List<HistoryTokenListDTO> getTokens() {
        return tokens;
    }

    public void setTokens(List<HistoryTokenListDTO> tokens) {
        this.tokens = tokens;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tokens == null) ? 0 : tokens.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HistoryResult other = (HistoryResult) obj;
        if (tokens == null) {
            if (other.tokens != null)
                return false;
        } else if (!tokens.equals(other.tokens))
            return false;
        return true;
    }
}
