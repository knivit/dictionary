package com.tsoft.dictionary.client.server.cache;

public class StringId implements HasId {
    private String id;

    public StringId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must be not null");
        }
        this.id = id;
    }

    @Override
    public Object getItemId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StringId other = (StringId) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return id;
    }
}
