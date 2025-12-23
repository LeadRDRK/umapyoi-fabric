package net.tracen.umapyoi.client.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/** Ported from MMLib **/
public class BedrockModelPOJO {
    @SerializedName("format_version")
    private String formatVersion;

    @SerializedName("geometry.model")
    private GeometryModelLegacy geometryModelLegacy;

    @SerializedName("minecraft:geometry")
    private List<GeometryModelNew> geometryModelNew;

    public String getFormatVersion() {
        return formatVersion;
    }

    public GeometryModelLegacy getGeometryModelLegacy() {
        return geometryModelLegacy;
    }

    public GeometryModelNew getGeometryModelNew() {
        if (geometryModelNew == null) {
            return null;
        }
        return geometryModelNew.get(0);
    }
}