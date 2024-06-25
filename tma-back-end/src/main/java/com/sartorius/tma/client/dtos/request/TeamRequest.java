package com.sartorius.tma.client.dtos.request;

import java.util.List;
import java.util.UUID;

import com.sartorius.tma.dtos.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamRequest {
  private UUID teamUuid;
  private String teamName;
  private List<UserDetails> members;
  private UserDetails manager;
  private double advancement;
}
