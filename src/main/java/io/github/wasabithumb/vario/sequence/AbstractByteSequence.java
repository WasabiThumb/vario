package io.github.wasabithumb.vario.sequence;

/**
 * Helper class to define consistent {@link #hashCode()}
 * and {@link #equals(Object)} for a {@link ByteSequence} implementation
 */
public abstract class AbstractByteSequence implements ByteSequence {

    @Override
    public int hashCode() {
        final int length = this.length();
        int result = 1;
        for (int i=0; i < length; i++) {
            result = 31 * result + this.byteAt(i);
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof ByteSequence) {
            return this.compareTo((ByteSequence) obj) == 0;
        }
        return super.equals(obj);
    }

}
