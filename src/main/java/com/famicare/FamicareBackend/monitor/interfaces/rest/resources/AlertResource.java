package com.famicare.FamicareBackend.monitor.interfaces.rest.resources;

public record AlertResource(Long id, Long elderlyProfileId, String type, String category, String title, String description, String status, String attendedBy, String createdAt, String resolvedAt) {

}