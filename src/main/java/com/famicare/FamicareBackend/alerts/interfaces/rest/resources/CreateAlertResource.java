package com.famicare.FamicareBackend.alerts.interfaces.rest.resources;

public record CreateAlertResource(Long elderlyProfileId, String type, String category, String title, String description) {

}