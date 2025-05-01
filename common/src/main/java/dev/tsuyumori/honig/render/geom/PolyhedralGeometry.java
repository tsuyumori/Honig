package dev.tsuyumori.honig.render.geom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PolyhedralGeometry implements RenderableShape<PolyhedralGeometry> {
    float[] vertices;
    int[] indices;
    float radius;
    int detail;

    ArrayList<Float> vertexBuffer = new ArrayList<>();
    ArrayList<Float> uvBuffer = new ArrayList<>();

    protected PolyhedralGeometry( float[] vertices, int[] indices, float radius, int detail ) {
        this.vertices = vertices;
        this.indices = indices;
        this.radius = radius;
        this.detail = detail;

        // default buffer data
        detailVertices(detail);

        // all vertices should lie on a conceptual sphere with a given radius
        extrudeVertices(radius);

        // finally, create the uv data
        generateUVs();

        // build non-indexed geometry
        /*if (detail == 0) {
            this.computeVertexNormals(); // flat normals
        } else {
            this.normalizeNormals(); // smooth normals
        }*/
    }

    protected PolyhedralGeometry( float[] vertices, int[] indices) {
        this(vertices, indices, 1, 0);
    }

    public PolyhedralGeometry setDetail(int detail) {
        this.detail = detail;
        this.vertexBuffer.clear();
        this.uvBuffer.clear();
        detailVertices(detail);
        extrudeVertices(this.radius);
        generateUVs();
        return this;
    }

    public PolyhedralGeometry autoDetail(float squaredDistance) {
        float step = squaredDistance / 10F;
        int lod = Math.max(3 - Mth.floor(step), 1);
        return setDetail(lod);
    }

    public PolyhedralGeometry setRadius(float radius) {
        this.radius = radius;
        this.uvBuffer.clear();
        extrudeVertices(radius);
        generateUVs();
        return this;
    }

    void detailVertices(int detail) {

        Vector3f a = new Vector3f();
        Vector3f b = new Vector3f();
        Vector3f c = new Vector3f();

        // iterate over all faces and apply a subdivision with the given detail value

        for (int i = 0; i < indices.length; i += 3) {

            // get the vertices of the face

            getVertexByIndex(indices[i + 0], a);
            getVertexByIndex(indices[i + 1], b);
            getVertexByIndex(indices[i + 2], c);

            // perform subdivision

            subdivideFace(a, b, c, detail);

        }
    }

    void subdivideFace(Vector3f a, Vector3f b, Vector3f c, int detail) {

        int cols = detail + 1;

        // [columns[rows]]
        Vector3f[][] v = new Vector3f[cols + 1][cols + 1];

        // construct all the vertices for this subdivision
        for (int i = 0; i <= cols; i++) {

            v[i] = new Vector3f[cols + 1];

            Vector3f aj = new Vector3f(a).lerp(c, (float) i / cols);
            Vector3f bj = new Vector3f(b).lerp(c, (float) i / cols);

            int rows = cols - i;

            for (int j = 0; j <= rows; j++) {

                if (j == 0 && i == cols) {

                    v[i][j] = aj;

                } else {

                    v[i][j] = new Vector3f(aj).lerp(bj, (float) j / rows);

                }

            }

        }

        // construct all the faces
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < 2 * (cols - i) - 1; j++) {

                int k = Mth.floor((float) j / 2);

                if (j % 2 == 0) {
                    pushVertex(v[i][k + 1]);
                    pushVertex(v[i + 1][k]);
                    pushVertex(v[i][k]);
                } else {
                    pushVertex(v[i][k + 1]);
                    pushVertex(v[i + 1][k + 1]);
                    pushVertex(v[i + 1][k]);
                }

            }

        }

    }

    public void extrudeVertices(float radius) {

        Vector3f vertex = new Vector3f();

        // Iterate over entire buffer and apply radius to each vertex
        for (int i = 0; i < vertexBuffer.size(); i += 3) {

            vertex.x = vertexBuffer.get(i + 0);
            vertex.y = vertexBuffer.get(i + 1);
            vertex.z = vertexBuffer.get(i + 2);

            vertex.normalize().mul(radius);

            vertexBuffer.set(i + 0, vertex.x);
            vertexBuffer.set(i + 1, vertex.y);
            vertexBuffer.set(i + 2, vertex.z);

        }
    }

    void generateUVs() {

        Vector3f vertex = new Vector3f();

        for (int i = 0; i < vertexBuffer.size(); i += 3) {
            vertex.x = vertexBuffer.get(i + 0);
            vertex.y = vertexBuffer.get(i + 1);
            vertex.z = vertexBuffer.get(i + 2);

            float u = azimuth(vertex) / 2 / Mth.PI + .5F;
            float v = inclination(vertex) / Mth.PI + .5F;
            uvBuffer.addAll(List.of(u, 1 - v));
        }

        correctUVs();
        correctSeam();
    }

    void correctSeam() {

        // handle case when face straddles the seam, see #3269

        for ( int i = 0; i < uvBuffer.size(); i += 6 ) {

            // uv data of a single face

            float x0 = uvBuffer.get(i + 0);
            float x1 = uvBuffer.get(i + 2);
            float x2 = uvBuffer.get(i + 4);

            float max = Math.max(Math.max( x0, x1 ),  x2);
            float min = Math.min(Math.min( x0, x1 ), x2);

            // 0.9 is somewhat arbitrary

            if ( max > 0.9 && min < 0.1 ) {

                if ( x0 < 0.2 ) uvBuffer.set(i + 0, uvBuffer.get(i + 0) + 1);
                if ( x1 < 0.2 ) uvBuffer.set(i + 0, uvBuffer.get(i + 0) + 1);
                if ( x2 < 0.2 ) uvBuffer.set(i + 0, uvBuffer.get(i + 0) + 1);

            }

        }

    }

    void pushVertex( Vector3f vertex ) {
        vertexBuffer.addAll(List.of(vertex.x, vertex.y, vertex.z));
    }

    void getVertexByIndex( int index, Vector3f vertex ) {

        int stride = index * 3;

        vertex.x = vertices[ stride + 0 ];
        vertex.y = vertices[ stride + 1 ];
        vertex.z = vertices[ stride + 2 ];

    }

    void correctUVs() {

        Vector3f a = new Vector3f();
        Vector3f b = new Vector3f();
        Vector3f c = new Vector3f();

        Vector3f centroid;

        Vector2f uvA = new Vector2f();
        Vector2f uvB = new Vector2f();
        Vector2f uvC = new Vector2f();

        for ( int i = 0, j = 0; i < vertexBuffer.size(); i += 9, j += 6 ) {

            a.set(vertexBuffer.get(i + 0), vertexBuffer.get(i + 1), vertexBuffer.get(i + 2));
            b.set(vertexBuffer.get(i + 3), vertexBuffer.get(i + 4), vertexBuffer.get(i + 5));
            c.set(vertexBuffer.get(i + 6), vertexBuffer.get(i + 7), vertexBuffer.get(i + 8));

            uvA.set(uvBuffer.get(j + 0), uvBuffer.get(j + 1));
            uvB.set(uvBuffer.get(j + 2), uvBuffer.get(j + 3));
            uvC.set(uvBuffer.get(j + 4), uvBuffer.get(j + 5));

            centroid = a;
            centroid.add( b ).add( c ).div( 3 );

            float azi = azimuth( centroid );

            correctUV( uvA, j + 0, a, azi );
            correctUV( uvB, j + 2, b, azi );
            correctUV( uvC, j + 4, c, azi );

        }

    }

    void correctUV(Vector2f uv, int stride, Vector3f vector, float azimuth ) {

        if ( ( azimuth < 0 ) && ( uv.x == 1 ) ) {

            uvBuffer.set(stride, uv.x - 1);

        }

        if ( ( vector.x == 0 ) && ( vector.z == 0 ) ) {

            uvBuffer.set(stride, azimuth / 2 / Mth.PI + .5F);

        }

    }

    // Angle around the Y axis, counter-clockwise when looking from above.

    float azimuth( Vector3f vector ) {
        return (float) Math.atan2(vector.z, vector.x);
    }


    // Angle above the XZ plane.

    float inclination( Vector3f vector ) {
        return (float) Math.atan2(-vector.y, Math.sqrt((vector.x * vector.x) + (vector.z * vector.z)));
    }

    public void render(VertexConsumer vBuf, PoseStack.Pose modelMat, Consumer<VertexConsumer> extraVertAttr) {
        Vector3f a = new Vector3f();
        Vector3f b = new Vector3f();
        Vector3f c = new Vector3f();
        Vector3f normal = new Vector3f();

        for (int i = 0, j = 0; i < vertexBuffer.size(); i += 9, j += 6) {
            // Vertex positions
            a.set(vertexBuffer.get(i), vertexBuffer.get(i + 1), vertexBuffer.get(i + 2));
            b.set(vertexBuffer.get(i + 3), vertexBuffer.get(i + 4), vertexBuffer.get(i + 5));
            c.set(vertexBuffer.get(i + 6), vertexBuffer.get(i + 7), vertexBuffer.get(i + 8));

            // Flip UVs
            float u1 = 1 - uvBuffer.get(j);
            float v1 = 1 - uvBuffer.get(j + 1);
            float u2 = 1 - uvBuffer.get(j + 2);
            float v2 = 1 - uvBuffer.get(j + 3);
            float u3 = 1 - uvBuffer.get(j + 4);
            float v3 = 1 - uvBuffer.get(j + 5);

            // Flat normal
            normal.set(b).sub(a);
            normal.cross(new Vector3f(c).sub(a)).normalize();

            // Push each vertex

            extraVertAttr.accept(vBuf.addVertex(modelMat, a.x, a.y, a.z).setColor(.075F, .15F, .2F, 1)
                    .setNormal(modelMat, normal.x, normal.y, normal.z).setUv(u1, v1));

            extraVertAttr.accept(vBuf.addVertex(modelMat, b.x, b.y, b.z).setColor(.075F, .15F, .2F, 1)
                    .setNormal(modelMat, normal.x, normal.y, normal.z).setUv(u2, v2));

            extraVertAttr.accept(vBuf.addVertex(modelMat, c.x, c.y, c.z).setColor(.075F, .15F, .2F, 1)
                    .setNormal(modelMat, normal.x, normal.y, normal.z).setUv(u3, v3));

        }
    }

}

