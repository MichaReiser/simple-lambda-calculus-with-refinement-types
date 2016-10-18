package ch.famreiser.hsr.seminar.typechecker.types;

import java.util.Map;

public class TypeVariable implements Shape {

        private static long COUNT = 0;
    private Shape pointsTo = null;
    private long index;

    private TypeVariable(long index) {
        this.index = index;
    }

    public static TypeVariable create() {
        return new TypeVariable(++COUNT);
    }

    public void resolve(Shape to) {
        this.pointsTo = to;
    }

    @Override
    public Shape resolved() {
        return this.pointsTo == null ? this : this.pointsTo.resolved();
    }

    @Override
    public boolean contains(Shape other) {
        return this.pointsTo != null && this.pointsTo.contains(other);
    }

    @Override
    public Shape fresh(Map<TypeVariable, TypeVariable> mappings) {
        if (mappings.containsKey(this)) {
            return mappings.get(this);
        }
        TypeVariable fresh = TypeVariable.create();
        mappings.put(this, fresh);
        return fresh;
    }

    @Override
    public String toString() {
        if (pointsTo == null) {
            return "@" + index;
        }
        return "(@" + index + "->" + pointsTo + ")";
    }
}
