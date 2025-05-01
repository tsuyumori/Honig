package dev.tsuyumori.honig.render.geom;

import java.util.function.Supplier;

import static dev.tsuyumori.honig.render.RenderUtil.*;

public interface PlatonicSolids {

    /// What the egyptians were building, except this one has a triangular base
    Supplier<PolyhedralGeometry> TETRAHEDRON = () -> new PolyhedralGeometry(new float[]{
            1, 1, 1, -1, -1, 1, -1, 1, -1, 1, -1, -1
    }, new int[]{
            2, 1, 0, 0, 3, 2, 1, 3, 0, 2, 3, 1
    });

    /// The 6-sided thingamajig we all know and love
    Supplier<PolyhedralGeometry> CUBE = () -> new PolyhedralGeometry(new float[]{
            -1, -1, -1, 1, -1, -1, 1,  1, -1, -1,  1, -1,
            -1, -1,  1, 1, -1,  1, 1,  1,  1, -1,  1,  1
    }, new int[]{
            0, 1, 2,  0, 2, 3,  // Back
            4, 6, 5,  4, 7, 6,  // Front
            0, 4, 5,  0, 5, 1,  // Bottom
            3, 2, 6,  3, 6, 7,  // Top
            1, 5, 6,  1, 6, 2,  // Right
            0, 3, 7,  0, 7, 4   // Left
    });

    /// Is it just me getting genshin vibes here?
    Supplier<PolyhedralGeometry> OCTAHEDRON = () -> new PolyhedralGeometry(new float[]{
            1, 0, 0, -1, 0, 0, 0, 1, 0,
            0, -1, 0, 0, 0, 1, 0, 0, -1
    }, new int[]{
            0, 2, 4, 0, 4, 3, 0, 3, 5,
            0, 5, 2, 1, 2, 5, 1, 5, 3,
            1, 3, 4, 1, 4, 2
    });

    /// A 12-sided Polyhedron... and that's about it :|
    Supplier<PolyhedralGeometry> DODECAHEDRON = () -> new PolyhedralGeometry(new float[]{
            -1, -1, -1, -1, -1, 1, -1, 1, -1, -1, 1, 1,
            1, -1, -1, 1, -1, 1, 1, 1, -1, 1, 1, 1,

            0, -PHI_INVERSE, -PHI, 0, -PHI_INVERSE, PHI,
            0, PHI_INVERSE, -PHI, 0, PHI_INVERSE, PHI,

            -PHI_INVERSE, -PHI, 0, -PHI_INVERSE, PHI, 0,
            PHI_INVERSE, -PHI, 0, PHI_INVERSE, PHI, 0,

            -PHI, 0, -PHI_INVERSE, PHI, 0, -PHI_INVERSE,
            -PHI, 0, PHI_INVERSE, PHI, 0, PHI_INVERSE
    }, new int[]{
            3, 11, 7, 3, 7, 15, 3, 15, 13, 7, 19, 17, 7, 17, 6, 7, 6, 15,
            17, 4, 8, 17, 8, 10, 17, 10, 6, 8, 0, 16, 8, 16, 2, 8, 2, 10,
            0, 12, 1, 0, 1, 18, 0, 18, 16, 6, 10, 2, 6, 2, 13, 6, 13, 15,
            2, 16, 18, 2, 18, 3, 2, 3, 13, 18, 1, 9, 18, 9, 11, 18, 11, 3,
            4, 14, 12, 4, 12, 0, 4, 0, 8, 11, 9, 5, 11, 5, 19, 11, 19, 7,
            19, 5, 14, 19, 14, 4, 19, 4, 17, 1, 12, 14, 1, 14, 5, 1, 5, 9
    });

    /// 20-sided Polyhedron. Used for shapes which require the highest
    /// level of perfection. (Or just a cool shape, take your pick)
    Supplier<PolyhedralGeometry> ICOSAHEDRON = () -> new PolyhedralGeometry(new float[] {
            -1, PHI, 0, 1, PHI, 0, -1, -PHI, 0, 1, -PHI, 0,
            0, -1, PHI, 0, 1, PHI, 0, -1, -PHI, 0, 1, -PHI,
            PHI, 0, -1, PHI, 0, 1, -PHI, 0, -1, -PHI, 0, 1
    }, new int[] {
        0, 11, 5, 0, 5, 1, 0, 1, 7, 0, 7, 10, 0, 10, 11,
        1, 5, 9, 5, 11, 4, 11, 10, 2, 10, 7, 6, 7, 1, 8,
        3, 9, 4, 3, 4, 2, 3, 2, 6, 3, 6, 8, 3, 8, 9,
        4, 9, 5, 2, 4, 11, 6, 2, 10, 8, 6, 7, 9, 8, 1
    });

    /// Plain old UV sphere. Crude, but effective.
    Supplier<UVSphereGeometry> PARAMETRIC_SPHERE = UVSphereGeometry::new;

}
